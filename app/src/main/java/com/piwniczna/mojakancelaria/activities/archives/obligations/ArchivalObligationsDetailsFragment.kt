package com.piwniczna.mojakancelaria.activities.archives.obligations

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ObligationHelper

class ArchivalObligationsDetailsFragment(var client: ClientEntity, val case: CaseEntity, var obligation: ObligationEntity) : Fragment() {
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archival_obligation_details, container, false)
        dbService = DataService(this.context!!)

        val nameTextView = view.findViewById(R.id.obligation_name_value) as TextView
        val typeTextView = view.findViewById(R.id.obligation_type_value) as TextView
        val amountTextView = view.findViewById(R.id.obligation_amount_value) as TextView
        val payedAmountTextView = view.findViewById(R.id.obligation_payed_amount_value) as TextView
        val creationDateTextView = view.findViewById(R.id.obligation_creation_date_value) as TextView
        val paymentDateTextView = view.findViewById(R.id.oligation_payment_date_value) as TextView


        nameTextView.text = obligation.name
        typeTextView.text = ObligationHelper.getTypeString(obligation.type, this.context!!)
        amountTextView.text = getString(R.string.amount_with_currency, obligation.amount.setScale(2).toString())
        payedAmountTextView.text = getString(R.string.amount_with_currency, obligation.payed.setScale(2).toString())
        creationDateTextView.text = obligation.convertDate()
        paymentDateTextView.text = obligation.convertPaymentDate()

        val caseNameTextView = view.findViewById<TextView>(R.id.case_name_title)
        caseNameTextView.text = case.name

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ArchivalObligationsFragment(client, case)
        )?.commit()
    }
}