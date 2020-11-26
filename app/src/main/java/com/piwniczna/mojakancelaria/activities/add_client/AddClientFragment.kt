package com.piwniczna.mojakancelaria.activities.add_client

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment
import kotlinx.android.synthetic.main.fragment_add_client.*

class AddClientFragment : Fragment() {
    lateinit var clientEditText : EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_client, container, false)

        dbService = DataService(this.context!!)
        val addButton = view.findViewById<Button>(R.id.saveClientButton)
        addButton.setOnClickListener {handleSaveClient(it)}

        clientEditText = view.findViewById(R.id.newClientEditText)

        return view
    }

    fun handleSaveClient(view: View) {
        val newClientName = newClientEditText.text.toString()
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