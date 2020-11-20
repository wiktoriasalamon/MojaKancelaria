package com.piwniczna.mojakancelaria.activities.clients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.R

class ClientsFragment : Fragment() {
    lateinit var clientsListAdapter: ClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: List<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)
        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = listOf("Jan Kowalski", "Anna Nowak", "Katarzyna Nosowska")
        clientsListAdapter = ClientsListAdapter(this.context!!, clientsList)
        clientsListView.adapter = clientsListAdapter

        return view
    }
}
