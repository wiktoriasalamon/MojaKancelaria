package com.piwniczna.mojakancelaria.activities.add_client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment
import kotlinx.android.synthetic.main.fragment_add_client.*

class AddClientFragment : Fragment() {
    lateinit var clientEditText : EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_client, container, false)

        val addButton = view.findViewById<Button>(R.id.saveClientButton)
        addButton.setOnClickListener {handleSaveClient(it)}

        clientEditText = view.findViewById(R.id.newClientEditText)

        return view
    }

    fun handleSaveClient(view: View) {
        val newClient = newClientEditText.text.toString()
        if (newClient == "") {
            val text = R.string.empty_client_warning
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(activity?.applicationContext, text, duration)
            toast.show()
        } else {
            // TODO: add client to database
            Log.e("ADD CLIENT", newClient)
            fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ClientsFragment()
            )?.commit()
        }

    }

}