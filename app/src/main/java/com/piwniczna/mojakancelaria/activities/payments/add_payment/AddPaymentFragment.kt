package com.piwniczna.mojakancelaria.activities.payments.add_payment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.RelationEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText
import com.piwniczna.mojakancelaria.utils.Validator
import java.math.BigDecimal
import java.time.LocalDate

class AddPaymentFragment(var client: ClientEntity): Fragment() {
    lateinit var nameEditText : EditText
    lateinit var amountEditText: EditText
    lateinit var dateButton: Button
    lateinit var addObligationButton: Button
    lateinit var obligationsListView: ListView
    lateinit var relationsList: ArrayList<RelationEntity>
    lateinit var obligationsList: ArrayList<ObligationEntity>
    lateinit var obligationsListAdapter: ObligationsOfPaymentListAdapter
    lateinit var addButton: Button
    lateinit var dbService: DataService
    var amountToSpend = BigDecimal(0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_payment, container, false)

        dbService = DataService(this.context!!)

        nameEditText = view.findViewById(R.id.new_payment_name_edittext)
        dateButton = view.findViewById(R.id.payment_date_button)
        addObligationButton = view.findViewById(R.id.add_obligation_to_payment_button)
        obligationsListView = view.findViewById(R.id.obligations_of_payment_list_view)
        addButton = view.findViewById(R.id.save_payment_button)

        amountEditText = view.findViewById(R.id.new_payment_amount_edittext)
        amountEditText.setOnClickListener {
            if (relationsList.size > 0) {
                val text = getText(R.string.cannot_edit_amount)
                val duration = Toast.LENGTH_LONG
                val toast = Toast.makeText(activity?.applicationContext, text, duration)
                toast.show()
            }
        }

        addObligationButton.setOnClickListener { handleAddObligation(it) }
        addButton.setOnClickListener {handleSavePayment(it)}
        dateButton.setOnClickListener {handleOpenCalendar(it)}

        relationsList = arrayListOf()
        obligationsList = arrayListOf()
        obligationsListAdapter = ObligationsOfPaymentListAdapter(this.context!!, relationsList, dbService, activity!!)
        obligationsListView.adapter = obligationsListAdapter

        obligationsListView.setOnItemLongClickListener { _, _, position, _ ->
            deletePayedObligation(position)
            true
        }

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                PaymentsFragment(client)
        )?.commit()
    }

    private fun handleAddObligation(view: View) {
        if(!Validator.validateAmount(amountEditText.text.toString(), this.context!!, activity?.applicationContext, false)) {
            val text = getText(R.string.cannot_add_obligation_without_amount)
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(activity?.applicationContext, text, duration)
            toast.show()
            return
        }

        var dialog = Dialog(this.context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_add_obligation_to_payment)

        val cancelButton = dialog.findViewById(R.id.adding_obligation_to_payment_cancel_button) as Button
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        var obligationsToChoose = arrayListOf<ObligationEntity>()
        val obligationsToChooseListView = dialog.findViewById(R.id.obligations_to_choose_list_view) as ListView
        val obligationsToChooseListAdapter = ObligationsToChooseListAdapter(this.context!!, obligationsToChoose)
        obligationsToChooseListView.adapter = obligationsToChooseListAdapter

        getObligationsToPay(obligationsToChoose, obligationsToChooseListAdapter)
        
        obligationsToChooseListView.setOnItemClickListener { _, _, position, _ ->
            dialog.dismiss()
            handleChooseObligation(obligationsToChoose[position])
        }

        dialog.show()
    }

    private fun handleChooseObligation(obligation: ObligationEntity) {
        var dialog = Dialog(this.context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_pay_obligation)

        val obligationAmountEditText = dialog.findViewById<EditText>(R.id.pay_obligation_amount_edittext)

        val leftToBeSpendTextView = dialog.findViewById<TextView>(R.id.left_to_be_spend_text_view)
        leftToBeSpendTextView.text = getString(R.string.left_to_be_spend, amountToSpend.setScale(2).toString())

        val amountToPayLabelTextView = dialog.findViewById<TextView>(R.id.amount_to_pay_label_text_view)
        amountToPayLabelTextView.setText( SpannedText.getSpannedText(
                getString(
                        R.string.amount_to_pay_label,
                        obligation.name.toUpperCase(),
                        obligation.payed.setScale(2).toString(),
                        obligation.amount.setScale(2).toString())))


        val saveButton = dialog.findViewById<Button>(R.id.pay_obligation_save_button)
        saveButton.setOnClickListener {
            if(Validator.validateAmount(obligationAmountEditText.text.toString(), this.context!!, activity?.applicationContext, true)) {
                Log.e("ADD PAYMENT", obligationAmountEditText.text.toString())
                addPayedObligation(obligation, BigDecimal(obligationAmountEditText.text.toString()))
                dialog.dismiss()
            }
        }

        val cancelButton = dialog.findViewById<Button>(R.id.pay_obligation_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun addPayedObligation(obligation: ObligationEntity, amount: BigDecimal) {
        relationsList.add(RelationEntity(amount, client.id, obligation.id, 0))
        obligationsList.add(obligation)
        if (relationsList.size > 0) {
            disableEditText(amountEditText)
        }
        obligationsListAdapter.notifyDataSetChanged()
    }

    private fun deletePayedObligation(position: Int) {
        val builder = AlertDialog.Builder(this.context)
        val message = getString(
                R.string.are_you_sure_delete_payed_obligation,
                getString(R.string.amount_with_currency,relationsList[position].amount.setScale(2).toString()),
                obligationsList[position].name
                )

        builder.setTitle(R.string.warning)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.delete) { _, _ ->
            relationsList.removeAt(position)
            obligationsList.removeAt(position)
            if (relationsList.size == 0) {
                enableEditText(amountEditText)
            }
            obligationsListAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton(R.string.cancel) { _, _ -> }

        builder.show()
    }

    private fun disableEditText(editText: EditText){
        editText.isFocusable = false
        editText.isClickable = true

    }

    private fun enableEditText(editText: EditText){
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
    }

    private fun getObligationsToPay(obligations: ArrayList<ObligationEntity>, listAdapter: ObligationsToChooseListAdapter) {
        AsyncTask.execute{
            val ob = dbService.getNotPayedObligations(client.id)
            obligations.clear()
            for (obligation in ob) {
                if(!obligationsList.contains(obligation)) {
                    obligations.add(obligation)
                }
            }
            activity?.runOnUiThread {
                listAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun handleSavePayment(view: View) {
        if (!validateData()) {
            return
        }
        // TODO
        Log.e("ADD PAYMENT", "save payment")
    }

    private fun handleOpenCalendar(view: View) {
        val date = LocalDate.now()
        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // date picker dialog
        val picker = DatePickerDialog(
                this.context!!,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    dateButton.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                },
                year,
                month-1,
                day
        )
        picker.show()
    }

    private fun validateData() : Boolean{
        var text = ""
        if (nameEditText.text.toString() == ""){
            text = this.context!!.resources.getString(R.string.name_not_provided)
        }
        else if (amountEditText.text.toString() == "" || BigDecimal(amountEditText.text.toString()).compareTo(BigDecimal(0))!=1 ){
            text = this.context!!.resources.getString(R.string.wrong_amount)
        }
        else if (dateButton.text.toString() == this.context!!.resources.getString(R.string.payment_date)){
            text = this.context!!.resources.getString(R.string.date_not_provided)
        }
        else{
            return true
        }

        // TODO: check obligations list

        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, text, duration)
        toast.show()
        return false
    }
}
