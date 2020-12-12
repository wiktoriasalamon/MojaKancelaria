package com.piwniczna.mojakancelaria.activities.cases.case_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.cases.CaseFragment
import com.piwniczna.mojakancelaria.activities.cases.ObligationsFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment

class CaseDetailsFragment(var case: CaseEntity) : Fragment() {
    lateinit var titleTextView: TextView
    lateinit var obligationsButton: Button
    lateinit var paymentsButton: Button
    lateinit var reportButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_case_details, container, false)

        titleTextView = view.findViewById(R.id.case_details_title)
        titleTextView.text = case.name

        obligationsButton = view.findViewById(R.id.obligations_button)
        obligationsButton.setOnClickListener { openObligationsFragment(it) }

        paymentsButton = view.findViewById(R.id.payments_button)
        paymentsButton.setOnClickListener { openPaymentsFragment(it) }

        reportButton = view.findViewById(R.id.report_button)

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                CaseFragment()
        )?.commit()
    }

    private fun openObligationsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ObligationsFragment(case)
        )?.commit()
    }

    private fun openPaymentsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                PaymentsFragment(case)
        )?.commit()
    }
}