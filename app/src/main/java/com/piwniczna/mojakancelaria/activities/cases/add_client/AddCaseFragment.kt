package com.piwniczna.mojakancelaria.activities.cases.add_client

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment

class AddCaseFragment(val client: ClientEntity) : Fragment() {
    lateinit var caseEditText : EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_client, container, false)

        dbService = DataService(this.context!!)
        val addButton = view.findViewById<Button>(R.id.save_client_button)
        addButton.setOnClickListener {handleSaveCase(it)}

        caseEditText = view.findViewById(R.id.new_client_edit_text)

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                CasesFragment(client)
        )?.commit()
    }

    fun handleSaveCase(view: View) {
        val newCaseName = caseEditText.text.toString()
        if (newCaseName == "") {
            val text = R.string.empty_case_warning
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(activity?.applicationContext, text, duration)
            toast.show()
            return
        }

        addNewClientToDB(CaseEntity(client.id, newCaseName))

        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
                CasesFragment(client)
        )?.commit()

    }

    private fun addNewClientToDB(case: CaseEntity){
        AsyncTask.execute { dbService.addCase(case) }
    }

}
