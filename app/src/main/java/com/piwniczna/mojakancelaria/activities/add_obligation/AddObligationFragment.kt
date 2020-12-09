package com.piwniczna.mojakancelaria.activities.add_obligation

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.icu.util.Calendar
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment
import com.piwniczna.mojakancelaria.activities.obligations.ObligationHelper
import kotlinx.android.synthetic.main.fragment_add_client.*
import java.time.LocalDate
import java.util.*


class AddObligationFragment : Fragment() {
    lateinit var nameEditText : EditText
    lateinit var typeSpinner: Spinner
    lateinit var amountEditText: EditText
    lateinit var dateEditText: EditText
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
        dateEditText = view.findViewById(R.id.date_edit_text)

        dateEditText.inputType = InputType.TYPE_NULL
        dateEditText.setOnClickListener {handleOpenCalendar(it)}

        setSpinner()



        return view
    }

    private fun setSpinner() {
        val types = arrayListOf<String>()
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
            ClientsFragment()
        )?.commit()
    }

    fun handleOpenCalendar(view: View) {
        val date = LocalDate.now().plusDays(14)

        val day = date.dayOfMonth
        val month = date.monthValue
        val year = date.year

        // date picker dialog
        val picker = DatePickerDialog(
            this.context!!,
            OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                dateEditText.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
            },
            year,
            month-1,
            day
        )
        picker.show()
    }

    fun handleSaveObligation(view: View) {
        //todo dododododo
        val newClientName = new_client_edit_text.text.toString()
        if (newClientName == "") {
            val text = R.string.empty_client_warning
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(activity?.applicationContext, text, duration)
            toast.show()
            return
        }

        addNewClientToDB(ClientEntity(newClientName))

        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientsFragment()
        )?.commit()

    }

    private fun addNewClientToDB(client: ClientEntity){
        AsyncTask.execute { dbService.addClient(client) }
    }

}