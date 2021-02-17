package com.piwniczna.mojakancelaria.activities.archives.payments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.PaymentEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.cases.ArchivalCaseDetailsFragment


import kotlin.collections.ArrayList


class ArchivalPaymentsFragment(var client: ClientEntity, val case: CaseEntity)  : Fragment() {
    lateinit var paymentsListView: ListView
    lateinit var paymentsList: ArrayList<PaymentEntity>
    lateinit var infoText: TextView
    lateinit var archivalPaymentsListAdapter: ArchivalPaymentsListAdapter
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archival_payments, container, false)
        dbService = DataService(this.context!!)

        paymentsListView = view.findViewById(R.id.payments_list_view) as ListView
        paymentsList = arrayListOf()
        archivalPaymentsListAdapter = ArchivalPaymentsListAdapter(this.context!!, paymentsList, activity!!)
        paymentsListView.adapter = archivalPaymentsListAdapter

        paymentsListView.setOnItemClickListener { _, _, position, _ ->
            openPaymentDetailsFragment(position)
        }

        infoText = view.findViewById<TextView>(R.id.payments_info)

        getPaymentsFromDB()

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ArchivalCaseDetailsFragment(client, case)
        )?.commit()
    }

    private fun getPaymentsFromDB() {
        AsyncTask.execute {
            val payments = dbService.getPayments(case.id)
            paymentsList.clear()
            paymentsList.addAll(payments)
            activity?.runOnUiThread {
                archivalPaymentsListAdapter.notifyDataSetChanged()
                if(paymentsList.size != 0){
                    infoText.text = ""
                }
            }
        }
    }


    private fun openPaymentDetailsFragment(paymentPosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ArchivalPaymentDetailsFragment(client, case, paymentsList[paymentPosition])
        )?.commit()
    }

}
