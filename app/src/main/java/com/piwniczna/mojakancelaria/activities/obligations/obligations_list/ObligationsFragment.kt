package com.piwniczna.mojakancelaria.activities.cases

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.obligations.add_obligation.AddObligationFragment
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment
import com.piwniczna.mojakancelaria.activities.obligations.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.activities.obligations.obligations_list.ObligationsListAdapter
import com.piwniczna.mojakancelaria.utils.EmailSender
import com.piwniczna.mojakancelaria.utils.PdfGenerator
import com.piwniczna.mojakancelaria.utils.ReportGenerator
import com.piwniczna.mojakancelaria.utils.SpannedText
import java.math.BigDecimal
import kotlin.collections.ArrayList

class ObligationsFragment(var client: ClientEntity, var case: CaseEntity)  : Fragment() {
    lateinit var obligationsListView: ListView
    lateinit var obligationsList: ArrayList<ObligationEntity>
    lateinit var obligationsListAdapter: ObligationsListAdapter
    lateinit var dbService: DataService
    lateinit var typeFilters: ArrayList<ObligationType>
    lateinit var sumOfAmountsToPayTextView: TextView
    lateinit var archiveButton: Button
    lateinit var reportButton: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_obligations, container, false)
        dbService = DataService(this.context!!)

        obligationsListView = view.findViewById(R.id.obligations_list_view) as ListView
        obligationsList = arrayListOf()
        obligationsListAdapter = ObligationsListAdapter(this.context!!, obligationsList)
        obligationsListView.adapter = obligationsListAdapter

        obligationsListView.setOnItemClickListener { _, _, position, _ ->
            openObligationDetailsFragment(position)
        }

        typeFilters = arrayListOf()
        for (i in ObligationType.values()) {
            typeFilters.add(i)
        }

        archiveButton = view.findViewById(R.id.archive_button)
        archiveButton.setOnClickListener { archiveCase(it) }

        reportButton = view.findViewById(R.id.report_button)
        reportButton.setOnClickListener { sendReport(it) }

        val addButton = view.findViewById<Button>(R.id.add_obligation_button)
        addButton.setOnClickListener { handleAddObligation(it) }

        val contractTypeButton = view.findViewById(R.id.contract_type_button) as ToggleButton
        configureToggleButton(contractTypeButton, ObligationType.CONTRACT)

        val stampTypeButton = view.findViewById(R.id.stamp_type_button) as ToggleButton
        configureToggleButton(stampTypeButton, ObligationType.STAMP)

        val courtTypeButton = view.findViewById(R.id.court_type_button) as ToggleButton
        configureToggleButton(courtTypeButton, ObligationType.COURT)

        val hearingTypeButton = view.findViewById(R.id.hearing_type_button) as ToggleButton
        configureToggleButton(hearingTypeButton, ObligationType.HEARING)

        val caseNameTextView = view.findViewById<TextView>(R.id.case_name_title)
        caseNameTextView.text = case.name

        sumOfAmountsToPayTextView = view.findViewById(R.id.sum_of_amounts_to_pay)
        setSumOfAmounts()

        getObligationsFromDB()

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                CasesFragment(client)
        )?.commit()
    }

    private fun getObligationsFromDB() {
        AsyncTask.execute {
            val obligations = dbService.getObligations(case.id)
            obligationsList.clear()
            obligationsList.addAll(obligations)
            activity?.runOnUiThread {
                obligationsListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setSumOfAmounts() {
        AsyncTask.execute {
            val sum = dbService.getSumOfObligationsAmountsToPay(case)
            activity?.runOnUiThread {
                sumOfAmountsToPayTextView.text = getString(R.string.sum_of_amounts_to_pay_label, sum.toString())
            }
        }
    }


    private fun handleAddObligation(view: View) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            AddObligationFragment(client, case)
        )?.commit()
    }

    private fun openObligationDetailsFragment(obligationPosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ObligationDetailsFragment(client, case, obligationsList[obligationPosition])
        )?.commit()
    }

    private fun configureToggleButton(button: ToggleButton, type: ObligationType) {
        button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                typeFilters.add(type)
                button.backgroundTintList = this.context!!.resources.getColorStateList(R.color.dark_blue)
            } else {
                typeFilters.remove(type)
                button.backgroundTintList = this.context!!.resources.getColorStateList(R.color.grey)
            }
            obligationsListAdapter.filter(typeFilters)
        }
    }
    private fun sendReport(view: View) {
        AsyncTask.execute {
            val reports = ReportGenerator.generateReport(case, this.context!!)
            val uri = PdfGenerator.generatePdfFromHTML(this.context!!,reports[0])

            val email = dbService.getConstant("default_email")
            EmailSender.sendEmail(this.context!!, uri, reports[1], email)

        }
    }

    private fun archiveCase(view: View) {
        var toPay = BigDecimal.ZERO
        AsyncTask.execute {
            val obligations = dbService.getObligations(case.id)
            for (o in obligations) {
                toPay = toPay.add(o.amount.minus(o.payed))
            }
            activity?.runOnUiThread{

                val builder = AlertDialog.Builder(this.context)

                val caseName = case.name
                var message: Spanned?

                if (toPay.compareTo(BigDecimal.ZERO)!=0){
                    message = SpannedText.getSpannedText(getString(R.string.archive_not_payed,caseName,toPay.setScale(2).toString()))
                }
                else{
                    message = SpannedText.getSpannedText(getString(R.string.archive_payed,caseName))
                }


                builder.setTitle(R.string.warning)
                builder.setMessage(message)

                builder.setPositiveButton(R.string.move) { dialog, which ->

                    builder.setTitle("Przenoszenie do archiwum")
                    builder.setMessage(R.string.are_you_sure)

                    builder.setPositiveButton(R.string.yes) { dialog, which -> moveCaseToArchives(case) }

                    builder.setNegativeButton(R.string.no) { dialog, which -> }

                    builder.show()

                }

                builder.setNegativeButton(R.string.cancel) { dialog, which -> }

                builder.show()


            }
        }

    }

    fun moveCaseToArchives(case: CaseEntity){
        AsyncTask.execute {
            dbService.setCaseArchival(case)
            onBackPressed()
        }

    }
}
