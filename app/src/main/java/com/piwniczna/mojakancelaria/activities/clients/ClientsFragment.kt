package com.piwniczna.mojakancelaria.activities.clients

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.SpannedText
import java.text.FieldPosition

class ClientsFragment : Fragment() {
    lateinit var clientsListAdapter: ClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)
        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = arrayListOf()
        clientsListAdapter = ClientsListAdapter(this.context!!, clientsList)
        clientsListView.adapter = clientsListAdapter

        clientsListView.setOnItemLongClickListener { _, _, position, id ->
            deleteClient(position, id)
            true
        }

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
            deleteClientFromDB(id.toString())
        }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()
    }

    private fun deleteClientFromDB(id: String) {
        Log.e("Client", "deleteing client ${id} ...")
        // TODO: delete client from database
        getClients()
    }

}
