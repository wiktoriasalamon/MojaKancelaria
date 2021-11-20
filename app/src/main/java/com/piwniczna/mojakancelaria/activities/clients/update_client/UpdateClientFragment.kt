package com.piwniczna.mojakancelaria.activities.clients.update_client

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
import com.piwniczna.mojakancelaria.models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.client_details.ClientDetailsFragment
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText

class UpdateClientFragment(var client: ClientEntity): Fragment() {
    lateinit var updateClientEditText : EditText
    lateinit var dbService: DataService
    lateinit var updateButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_update_client, container, false)

        dbService = DataService(this.context!!)
        updateButton = view.findViewById(R.id.save_update_client_button)
        updateButton.setOnClickListener {handleUpdateClient(it)}

        updateClientEditText = view.findViewById(R.id.update_client_edit_text)
        updateClientEditText.setText(client.name)

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientDetailsFragment(client)
        )?.commit()
    }

    private fun handleUpdateClient(view: View) {
        val newClientName = updateClientEditText.text.toString()

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
                        updateClient((newClientName))
                    }

                    builder.setNegativeButton(R.string.no) { dialog, which -> }

                    builder.show()
                } else {
                    updateClient(newClientName)
                }
            }

        }


    }

    private fun updateClient(clientName: String) {
        client.name = clientName
        updateClientInDB(client)
        activity!!.runOnUiThread {
            fragmentManager?.beginTransaction()?.replace(
                    R.id.fragment_container,
                    ClientDetailsFragment(client)
            )?.commit()
        }
    }

    private fun updateClientInDB(client: ClientEntity){
        AsyncTask.execute { dbService.updateClient(client) }
    }
}