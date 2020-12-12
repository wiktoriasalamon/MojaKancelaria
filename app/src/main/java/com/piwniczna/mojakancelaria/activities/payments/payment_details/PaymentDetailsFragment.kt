package com.piwniczna.mojakancelaria.activities.payments.payment_details


import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.PaymentEntity
import com.piwniczna.mojakancelaria.Models.RelationEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.payments.add_payment.ObligationsOfPaymentListAdapter
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText

class PaymentDetailsFragment(var client: ClientEntity, val case: CaseEntity, var payment: PaymentEntity) : Fragment() {
    lateinit var dbService: DataService
    lateinit var relationsList: ArrayList<RelationEntity>
    lateinit var obligationsAdapter: ObligationsOfPaymentListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment_details, container, false)
        dbService = DataService(this.context!!)

        val nameTextView = view.findViewById(R.id.payment_name_value) as TextView
        val amountTextView = view.findViewById(R.id.payment_amount_value) as TextView
        val paymentDateTextView = view.findViewById(R.id.payment_date_value) as TextView
        val obligationsListView = view.findViewById<ListView>(R.id.obligations_of_payment_list_view_in_payment_details)

        relationsList = arrayListOf()

        obligationsAdapter = ObligationsOfPaymentListAdapter(this.context!!, relationsList, dbService, activity!!)
        obligationsListView.adapter = obligationsAdapter

        getRelationsFromDB()


        nameTextView.text = payment.name
        amountTextView.text = getString(R.string.amount_with_currency, payment.amount.setScale(2).toString())
        paymentDateTextView.text = payment.convertDate()

        val deleteButton = view.findViewById(R.id.payment_delete_button) as Button
        deleteButton.setOnClickListener { deletePayment() }

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

    private fun deletePayment() {
        val builder = AlertDialog.Builder(this.context)

        val message = SpannedText.getSpannedText(getString(R.string.delete_payment, payment.name))

        builder.setTitle(R.string.warning)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.delete) { dialog, which -> deletePaymentFromDB() }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()
    }

    private fun deletePaymentFromDB() {
        AsyncTask.execute {
            dbService.deletePayment(payment)
            fragmentManager?.beginTransaction()?.replace(
                    R.id.fragment_container,
                    PaymentsFragment(client, case)
            )?.commit()
        }
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                PaymentsFragment(client, case)
        )?.commit()
    }
}