package com.piwniczna.mojakancelaria.activities.clients

import android.R.attr.button
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.add_client.AddClientFragment


class ClientsFragment : Fragment() {
    lateinit var clientsListAdapter: ClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: ArrayList<String>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)

        val addButton = view.findViewById<Button>(R.id.addClientButton)
        addButton.setOnClickListener {handleAddClient(it)}

        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = arrayListOf()
        clientsListAdapter = ClientsListAdapter(this.context!!, clientsList)
        clientsListView.adapter = clientsListAdapter

        getClients()
        clientsListAdapter.notifyDataSetChanged()

        return view
    }

    fun getClients() {
        val clients = arrayListOf<String>("Jan Kowalski", "Anna Nowak", "Katarzyna Nosowska")
        clientsList.addAll(clients)
    }

    fun handleAddClient(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                AddClientFragment()
        )?.commit()
    }


}
