package com.piwniczna.mojakancelaria.activities.archives.obligations

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.piwniczna.mojakancelaria.activities.archives.cases.ArchivalCaseDetailsFragment

class ArchivalObligationsFragment(var client: ClientEntity, var case: CaseEntity)  : Fragment() {
    lateinit var obligationsListView: ListView
    lateinit var obligationsList: ArrayList<ObligationEntity>
    lateinit var obligationsListAdapter: ArchivalObligationsListAdapter
    lateinit var dbService: DataService
    lateinit var typeFilters: ArrayList<ObligationType>
    lateinit var sumOfAmountsToPayTextView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archival_obligations, container, false)
        dbService = DataService(this.context!!)

        obligationsListView = view.findViewById(R.id.archival_obligations_list_view) as ListView
        obligationsList = arrayListOf()
        obligationsListAdapter = ArchivalObligationsListAdapter(this.context!!, obligationsList, activity!!)
        obligationsListView.adapter = obligationsListAdapter

        obligationsListView.setOnItemClickListener { _, _, position, _ ->
            openObligationDetailsFragment(position)
        }

        typeFilters = arrayListOf()
        for (i in ObligationType.values()) {
            typeFilters.add(i)
        }

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
            ArchivalCaseDetailsFragment(client, case)
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


    private fun openObligationDetailsFragment(obligationPosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ArchivalObligationsDetailsFragment(client, case, obligationsList[obligationPosition])
        )?.commit()
    }

    private fun configureToggleButton(button: ToggleButton, type: ObligationType) {
        button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                typeFilters.add(type)
                button.backgroundTintList = this.context!!.resources.getColorStateList(R.color.archive_intence)
            } else {
                typeFilters.remove(type)
                button.backgroundTintList = this.context!!.resources.getColorStateList(R.color.grey)
            }
            obligationsListAdapter.filter(typeFilters)
        }
    }

}