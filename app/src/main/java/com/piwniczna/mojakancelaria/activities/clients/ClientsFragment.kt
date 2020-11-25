package com.piwniczna.mojakancelaria.activities.clients

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.SpannedText
import com.piwniczna.mojakancelaria.activities.add_client.AddClientFragment


class ClientsFragment : Fragment() {
    lateinit var clientsListAdapter: ClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: ArrayList<String>
    lateinit var searchClientsEditText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)

        val addButton = view.findViewById<Button>(R.id.add_client_button)
        addButton.setOnClickListener {handleAddClient(it)}

        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = arrayListOf()
        clientsListAdapter = ClientsListAdapter(this.context!!, clientsList)
        clientsListView.adapter = clientsListAdapter

        clientsListView.setOnItemLongClickListener { _, _, position, id ->
            deleteClient(position, id)
            true
        }

        searchClientsEditText = view.findViewById(R.id.search_clients_edittext)
        searchClientsEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if(s != "") {
                    Log.e("CLIENTS", s.toString())
                    clientsListAdapter.filter.filter(s)
                } else {

                }
            }
        })

        getClients()

        return view
    }

    private fun getClients() {
        val clients = arrayListOf("Jan Kowalski", "Anna Nowak", "Katarzyna Nosowska")
        clientsList.clear()
        clientsList.addAll(clients)
        clientsListAdapter.notifyDataSetChanged()
    }

    private fun deleteClient(position: Int, id: Long) {
        val builder = AlertDialog.Builder(this.context)

        val clientName = clientsListAdapter.data[position]
        val message = SpannedText.getSpannedText(getString(R.string.delete_client, clientName))

        builder.setTitle(R.string.warning)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.delete) { dialog, which ->

            builder.setTitle(R.string.deleting)
            builder.setMessage(R.string.are_you_sure)

            builder.setPositiveButton(R.string.yes) { dialog, which -> deleteClientFromDB(id.toString()) }

            builder.setNegativeButton(R.string.no) { dialog, which -> }

            builder.show()

        }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()
    }

    private fun deleteClientFromDB(id: String) {
        Log.e("Client", "deleteing client ${id} ...")
        // TODO: delete client from database
        getClients()
    }



    fun handleAddClient(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                AddClientFragment()
        )?.commit()
    }


}
