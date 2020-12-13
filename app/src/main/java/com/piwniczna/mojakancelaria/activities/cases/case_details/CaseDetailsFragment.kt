package com.piwniczna.mojakancelaria.activities.cases.case_details

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
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment
import com.piwniczna.mojakancelaria.activities.cases.ObligationsFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment

class CaseDetailsFragment(var client: ClientEntity, var case: CaseEntity) : Fragment() {
    lateinit var clientTextView: TextView
    lateinit var caseTextView: TextView
    lateinit var obligationsButton: Button
    lateinit var paymentsButton: Button
    lateinit var archiveButton: Button
    lateinit var reportButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_case_details, container, false)

        clientTextView = view.findViewById(R.id.case_details_client)
        clientTextView.text = client.name

        caseTextView = view.findViewById(R.id.case_details_name)
        caseTextView.text = case.name

        obligationsButton = view.findViewById(R.id.obligations_button)
        obligationsButton.setOnClickListener { openObligationsFragment(it) }

        paymentsButton = view.findViewById(R.id.payments_button)
        paymentsButton.setOnClickListener { openPaymentsFragment(it) }

        archiveButton = view.findViewById(R.id.archive_button)
        archiveButton.setOnClickListener { archivizeCase(it) }

        reportButton = view.findViewById(R.id.report_button)
        reportButton.setOnClickListener { sendReport(it) }

        return view
    }

    private fun sendReport(view: View) {
        //todo
    }

    private fun archivizeCase(view: View) {
        //todo
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                CasesFragment(client)
        )?.commit()
    }

    private fun openObligationsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ObligationsFragment(client, case)
        )?.commit()
    }

    private fun openPaymentsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                PaymentsFragment(client, case)
        )?.commit()
    }
}