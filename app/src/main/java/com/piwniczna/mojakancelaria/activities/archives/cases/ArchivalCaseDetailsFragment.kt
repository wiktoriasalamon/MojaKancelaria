package com.piwniczna.mojakancelaria.activities.archives.cases

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.models.CaseEntity
import com.piwniczna.mojakancelaria.models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.obligations.ArchivalObligationsFragment
import com.piwniczna.mojakancelaria.activities.archives.payments.ArchivalPaymentsFragment
import com.piwniczna.mojakancelaria.utils.ArchivalFragment
import com.piwniczna.mojakancelaria.utils.EmailSender
import com.piwniczna.mojakancelaria.utils.PdfGenerator
import com.piwniczna.mojakancelaria.utils.ReportGenerator

class ArchivalCaseDetailsFragment(var client: ClientEntity, var case: CaseEntity) : ArchivalFragment() {
    lateinit var caseTextView: TextView
    lateinit var dbService: DataService
    lateinit var obligationsButton: Button
    lateinit var paymentsButton: Button
    lateinit var reportButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_archival_case_details, container, false)
        dbService = DataService(this.context!!)

        caseTextView = view.findViewById(R.id.case_name_title)
        caseTextView.text = case.name

        obligationsButton = view.findViewById(R.id.obligations_button)
        obligationsButton.setOnClickListener { openObligationsFragment(it) }

        paymentsButton = view.findViewById(R.id.payments_button)
        paymentsButton.setOnClickListener { openPaymentsFragment(it) }

        reportButton = view.findViewById(R.id.report_button)
        reportButton.setOnClickListener { sendReport(it) }

        return view
    }

    private fun sendReport(view: View) {
        AsyncTask.execute {
            var reports = ReportGenerator.generateReport(case, this.context!!)
            var uri = PdfGenerator.generatePdfFromHTML(this.context!!,reports[0])

            val email = dbService.getConstant("default_email")
            EmailSender.sendEmail(this.context!!, uri, reports[1], email)

        }
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                ArchivalCasesFragment(client)
        )?.commit()
    }

    private fun openObligationsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ArchivalObligationsFragment(client, case)
        )?.commit()
    }

    private fun openPaymentsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ArchivalPaymentsFragment(client, case)
        )?.commit()
    }

}