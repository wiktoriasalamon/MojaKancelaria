package com.piwniczna.mojakancelaria.activities.clients

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.Models.PaymentEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.SpannedText
import com.piwniczna.mojakancelaria.activities.add_client.AddClientFragment
import com.piwniczna.mojakancelaria.activities.client_details.ClientDetailsFragment
import java.time.LocalDate


class ClientsFragment : Fragment() {
    lateinit var clientsListAdapter: ClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: ArrayList<ClientEntity>
    lateinit var searchClientsEditText: EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)
        dbService = DataService(this.context!!)

        val addButton = view.findViewById<Button>(R.id.add_client_button)
        addButton.setOnClickListener { handleAddClient(it) }

        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = arrayListOf()
        clientsListAdapter = ClientsListAdapter(this.context!!, clientsList)
        clientsListView.adapter = clientsListAdapter

        clientsListView.setOnItemClickListener { _, _, position, _ ->
            openClientDetailsFragment(position)
        }

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
                clientsListAdapter.filter.filter(s)
            }
        })

        getClientsFromDB()

        return view
    }

    fun onBackPressed() {
        this.activity?.finish()
    }

    private fun getClientsFromDB() {
        AsyncTask.execute {
            val clients = dbService.getClients()
            clientsList.clear()
            clientsList.addAll(clients)
            activity?.runOnUiThread {
                clientsListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteClient(position: Int, id: Long) {
        val builder = AlertDialog.Builder(this.context)

        val clientName = clientsListAdapter.data[position].name
        val message = SpannedText.getSpannedText(getString(R.string.delete_client, clientName))

        builder.setTitle(R.string.warning)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.delete) { dialog, which ->

            builder.setTitle(R.string.deleting)
            builder.setMessage(R.string.are_you_sure)

            builder.setPositiveButton(R.string.yes) { dialog, which -> deleteClientFromDB(position) }

            builder.setNegativeButton(R.string.no) { dialog, which -> }

            builder.show()

        }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()
    }

    private fun deleteClientFromDB(position: Int) {
        AsyncTask.execute {
            dbService.deleteClient(clientsList[position])
            getClientsFromDB()
        }

    }

    private fun handleAddClient(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                AddClientFragment()
        )?.commit()
    }

    private fun openClientDetailsFragment(clientPosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientDetailsFragment(clientsList[clientPosition])
        )?.commit()
    }

}
