package com.piwniczna.mojakancelaria.activities.clients.add_client

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText

class AddClientFragment: Fragment() {
    lateinit var clientEditText : EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_client, container, false)

        dbService = DataService(this.context!!)
        val addButton = view.findViewById<Button>(R.id.save_client_button)
        addButton.setOnClickListener {handleSaveClient(it)}

        clientEditText = view.findViewById(R.id.new_client_edit_text)

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ClientsFragment()
        )?.commit()
    }

    fun handleSaveClient(view: View) {
        val newClientName = clientEditText.text.toString()

        if (newClientName == "") {
            val text = R.string.empty_client_warning
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(activity?.applicationContext, text, duration)
            toast.show()
            return
        }
        if (newClientName.length > resources.getInteger(R.integer.max_client_len)) {
            val text = R.string.too_long_client_warning
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(activity?.applicationContext, text, duration)
            toast.show()
            return
        }
        AsyncTask.execute {
            val clientExists = dbService.ifClientExists(newClientName)
            activity!!.runOnUiThread {
                if (clientExists) {
                    val builder = AlertDialog.Builder(this.context)
                    val message =
                            SpannedText.getSpannedText(getString(R.string.add_multiplicated_client, newClientName))

                    builder.setTitle(R.string.warning)
                    builder.setMessage(message)

                    builder.setPositiveButton(R.string.yes) { dialog, which ->
                        addClient((newClientName))
                    }

                    builder.setNegativeButton(R.string.no) { dialog, which -> }

                    builder.show()
                } else {
                    addClient(newClientName)
                }
            }

        }


    }

    private fun addClient(clientName: String) {
        addNewClientToDB(ClientEntity(clientName))
        activity!!.runOnUiThread {
            fragmentManager?.beginTransaction()?.replace(
                    R.id.fragment_container,
                    ClientsFragment()
            )?.commit()
        }
    }

    private fun addNewClientToDB(client: ClientEntity){
        AsyncTask.execute { dbService.addClient(client) }
    }
}