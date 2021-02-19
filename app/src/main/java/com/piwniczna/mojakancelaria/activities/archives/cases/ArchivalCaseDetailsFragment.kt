package com.piwniczna.mojakancelaria.activities.archives.cases

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.obligations.ArchivalObligationsFragment
import com.piwniczna.mojakancelaria.activities.archives.payments.ArchivalPaymentsFragment
import com.piwniczna.mojakancelaria.utils.EmailSender
import com.piwniczna.mojakancelaria.utils.PdfGenerator
import com.piwniczna.mojakancelaria.utils.ReportGenerator

class ArchivalCaseDetailsFragment(var client: ClientEntity, var case: CaseEntity) : Fragment() {
    lateinit var caseTextView: TextView
    lateinit var obligationsButton: Button
    lateinit var paymentsButton: Button
    lateinit var reportButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archival_case_details, container, false)

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

            EmailSender.sendEmail(this.context!!, uri, reports[1], "elzbieta.lewandowicz@gmail.com")

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