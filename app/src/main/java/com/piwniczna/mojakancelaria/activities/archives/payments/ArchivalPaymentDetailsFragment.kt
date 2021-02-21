package com.piwniczna.mojakancelaria.activities.archives.payments


import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.PaymentEntity
import com.piwniczna.mojakancelaria.Models.RelationEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ArchivalFragment

class ArchivalPaymentDetailsFragment(var client: ClientEntity, val case: CaseEntity, var payment: PaymentEntity) : ArchivalFragment() {
    lateinit var dbService: DataService
    lateinit var relationsList: ArrayList<RelationEntity>
    lateinit var obligationsAdapter: ArchivalObligationsOfPaymentListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_archival_payment_details, container, false)
        dbService = DataService(this.context!!)

        val nameTextView = view.findViewById(R.id.payment_name_value) as TextView
        val amountTextView = view.findViewById(R.id.payment_amount_value) as TextView
        val paymentDateTextView = view.findViewById(R.id.payment_date_value) as TextView
        val obligationsListView = view.findViewById<ListView>(R.id.obligations_of_payment_list_view_in_payment_details)

        relationsList = arrayListOf()

        obligationsAdapter = ArchivalObligationsOfPaymentListAdapter(this.context!!, relationsList, dbService, activity!!)
        obligationsListView.adapter = obligationsAdapter

        getRelationsFromDB()


        nameTextView.text = payment.name
        amountTextView.text = getString(R.string.amount_with_currency, payment.amount.setScale(2).toString())
        paymentDateTextView.text = payment.convertDate()

        return view
    }

    private fun getRelationsFromDB() {
        AsyncTask.execute {
            val relations = dbService.getRelations(payment)
            relationsList.clear()
            relationsList.addAll(relations)
            activity?.runOnUiThread {
                obligationsAdapter.notifyDataSetChanged()
            }
        }
    }



    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ArchivalPaymentsFragment(client, case)
        )?.commit()
    }
}