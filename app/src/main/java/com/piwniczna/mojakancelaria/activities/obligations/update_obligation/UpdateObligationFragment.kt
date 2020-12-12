package com.piwniczna.mojakancelaria.activities.obligations.update_obligation

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.AsyncTask
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.obligations.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.utils.ObligationHelper
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class UpdateObligationFragment(var case: CaseEntity, var obligation: ObligationEntity) : Fragment() {
    lateinit var nameEditText : EditText
    lateinit var typeSpinner: Spinner
    lateinit var amountEditText: EditText
    lateinit var dateButton: Button
    lateinit var addButton: Button


    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_obligation, container, false)

        dbService = DataService(this.context!!)

        addButton = view.findViewById<Button>(R.id.save_obligation_button)
        addButton.setOnClickListener {handleSaveObligation(it)}

        nameEditText = view.findViewById(R.id.name_edit_text)
        typeSpinner = view.findViewById(R.id.type_spinner)
        amountEditText = view.findViewById(R.id.amount_edit_text)
        dateButton = view.findViewById(R.id.date_button)

        dateButton.setOnClickListener {handleOpenCalendar(it)}
        setSpinner()

        setAllData()

        return view
    }

    private fun setSpinner() {
        val types = arrayListOf<String>()
        types.add(this.context!!.resources.getString(R.string.spinner_prompt))
        for(i in ObligationType.values()){
            types.add(ObligationHelper.getTypeString(i,this.context!!))
        }

        val aa = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, types)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(typeSpinner)
        {
            adapter = aa
            setSelection(0, false)
            gravity = Gravity.CENTER
        }

    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                ObligationDetailsFragment(case, obligation)
        )?.commit()
    }

    fun handleOpenCalendar(view: View) {
        val date = obligation.paymentDate

        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // date picker dialog
        val picker = DatePickerDialog(
            this.context!!,
            OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                dateButton.text = dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
            },
            year,
            month-1,
            day
        )
        picker.show()
    }

    fun setAllData() {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = formatter.format(obligation.paymentDate)
        val typeIndex = ObligationType.values().indexOf(obligation.type)

        nameEditText.setText(obligation.name)
        amountEditText.setText(obligation.amount.toString())
        typeSpinner.setSelection(typeIndex+1)
        dateButton.setText(date)
    }

    fun handleSaveObligation(view: View) {
        if(!validateData()){
            return
        }

        val type = ObligationType.values()[typeSpinner.selectedItemPosition-1]
        val date = dateButton.text.toString()
        val locDate = LocalDate.of(date.split('/')[2].toInt(),date.split('/')[1].toInt(),date.split('/')[0].toInt())
        obligation.type = type
        obligation.name = nameEditText.text.toString()
        obligation.amount = BigDecimal(amountEditText.text.toString()).setScale(2, RoundingMode.HALF_UP)
        obligation.paymentDate = locDate

        updateObligationInDB(obligation)

        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                ObligationDetailsFragment(case,obligation)
        )?.commit()

    }

    private fun validateData() : Boolean{
        var text = ""
        if (nameEditText.text.toString() == "" || amountEditText.text.toString() == ""){
            text = this.context!!.resources.getString(R.string.not_provided_data)
        }
        else if (BigDecimal(amountEditText.text.toString()).compareTo(BigDecimal(0))!=1 ){
            text = this.context!!.resources.getString(R.string.wrong_amount)
        }
        else if (BigDecimal(amountEditText.text.toString()).minus(obligation.payed).compareTo(BigDecimal(0))!=1 ){
            text = this.context!!.resources.getString(R.string.over_payed)
        }
        else if (typeSpinner.selectedItem.toString() == this.context!!.resources.getString(R.string.spinner_prompt)) {
            text = this.context!!.resources.getString(R.string.spinner_error)
        }
        else if (dateButton.text.toString() == this.context!!.resources.getString(R.string.payment_deadline)){
            text = this.context!!.resources.getString(R.string.date_not_provided)
        }
        else{
            return true
        }

        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, text, duration)
        toast.show()
        return false
    }


    private fun updateObligationInDB(obligation: ObligationEntity){
        AsyncTask.execute { dbService.updateObligation(obligation) }
    }

}
