package com.piwniczna.mojakancelaria.activities.payments.add_payment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.PaymentEntity
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
    lateinit var summaryTextView: TextView
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
        summaryTextView = view.findViewById(R.id.summary_payed)

        amountEditText = view.findViewById(R.id.new_payment_amount_edittext)
        amountEditText.setOnClickListener {
            if (relationsList.size > 0) {
                val text = getString(R.string.cannot_edit_amount)
                toastMessage(text)
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
        if(!Validator.validateAmount(amountEditText.text.toString(), this.context!!, activity?.applicationContext)) {
            val text = getString(R.string.cannot_add_obligation_without_amount)
            toastMessage(text)
            return
        }

        amountToSpend = countAmountToSpend()

        if (amountToSpend.compareTo(BigDecimal(0))==0){
            val text = getString(R.string.cannot_add_obligation_when_nothing_to_spend)
            toastMessage(text)
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
        var amountPrompt = ""
        if (amountToSpend.compareTo(obligation.amount.minus(obligation.payed))==-1){
            amountPrompt = amountToSpend.toString()
        }
        else{
            amountPrompt = obligation.amount.minus(obligation.payed).setScale(2).toString()
        }

        obligationAmountEditText.setText(amountPrompt)

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
            if(!Validator.validateAmount(obligationAmountEditText.text.toString(), this.context!!, activity?.applicationContext)) {
                val text = getString(R.string.wrong_amount)
                toastMessage(text)
            }
            else if(BigDecimal(obligationAmountEditText.text.toString()).compareTo(obligation.amount.minus(obligation.payed))==1){
                val text = getString(R.string.too_large_amount)
                toastMessage(text)
            }
            else if(BigDecimal(obligationAmountEditText.text.toString()).compareTo(countAmountToSpend())==1){
                val text = getString(R.string.amount_bigger_than_left)
                toastMessage(text)
            }
            else{
                addPayedObligation(obligation, BigDecimal(obligationAmountEditText.text.toString()))
                dialog.dismiss()

                setSummaryText()
            }
        }

        val cancelButton = dialog.findViewById<Button>(R.id.pay_obligation_cancel_button)
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setSummaryText() {
        val toSpend = countAmountToSpend()
        val wholeAmount = BigDecimal(amountEditText.text.toString())
        summaryTextView.setText(getString(R.string.payed_amount_with_currency,wholeAmount.minus(toSpend).setScale(2).toString(),wholeAmount.setScale(2).toString()))
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
            setSummaryText()
            obligationsListAdapter.notifyDataSetChanged()
        }

        builder.setNegativeButton(R.string.cancel) { _, _ -> }

        builder.show()
    }

    private fun disableEditText(editText: EditText){
        editText.isFocusable = false
        editText.isClickable = true

    }

    private fun enableEditText(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
    }
    
    private fun countAmountToSpend(): BigDecimal {
        var amount = BigDecimal(amountEditText.text.toString())
        for( o in relationsList){
            amount = amount.minus(o.amount)
        }
        if (amount.compareTo(BigDecimal(0)) == -1) {
            return BigDecimal(-1)
        }
        return amount
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
        val date = dateButton.text.toString()
        val payment = PaymentEntity(
                client.id,
                nameEditText.text.toString(),
                BigDecimal(amountEditText.text.toString()),
                LocalDate.of(date.split('.')[2].toInt(),date.split('.')[1].toInt(),date.split('.')[0].toInt()))
        val amountList = arrayListOf<BigDecimal>()
        for (rel in relationsList) {
            amountList.add(rel.amount)
        }

        savePaymentToDB(payment, amountList)

        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                PaymentsFragment(client)
        )?.commit()
    }

    private fun savePaymentToDB(payment: PaymentEntity, amountList: ArrayList<BigDecimal>) {
        AsyncTask.execute {
            dbService.addPayment(payment,obligationsList, amountList)
        }
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
                    dateButton.text = dayOfMonth.toString() + "." + (monthOfYear + 1) + "." + year
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
            text = getString(R.string.name_not_provided)
        }
        else if (amountEditText.text.toString() == "" || BigDecimal(amountEditText.text.toString()).compareTo(BigDecimal(0))!=1 ){
            text = getString(R.string.wrong_amount)
        }
        else if (dateButton.text.toString() == getString(R.string.payment_date)){
            text = getString(R.string.date_not_provided)
        }
        else if (countAmountToSpend().compareTo(BigDecimal.ZERO)!=0){
            text = getString(R.string.money_left)
        }
        else{
            return true
        }

        // TODO: check obligations list

        toastMessage(text)
        return false
    }

    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, message, duration)
        toast.show()
    }
}
