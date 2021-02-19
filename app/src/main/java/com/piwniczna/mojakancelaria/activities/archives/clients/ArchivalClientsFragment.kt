package com.piwniczna.mojakancelaria.activities.archives.clients

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.cases.ArchivalCasesFragment
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText

class ArchivalClientsFragment(): Fragment() {
    lateinit var clientsListAdapter: ArchivalClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: ArrayList<ClientEntity>
    lateinit var searchClientsEditText: EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_archival_clients, container, false)
        dbService = DataService(this.context!!)

        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = arrayListOf()
        clientsListAdapter = ArchivalClientsListAdapter(this.context!!, clientsList, activity!!)
        clientsListView.adapter = clientsListAdapter

        clientsListView.setOnItemClickListener { _, _, position, _ ->
            openArchivalClientCasesFragment(position)
        }

        clientsListView.setOnItemLongClickListener { _, _, position, id ->
            deleteArchivalClient(position, id)
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

       setActionbar()

        return view
    }

    private fun setActionbar() {
        val bar = (activity as AppCompatActivity).supportActionBar
        bar!!.setBackgroundDrawable(ColorDrawable(this.context!!.resources.getColor(R.color.archive_intence)))
        bar.title = getString(R.string.archives)
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientsFragment()
        )?.commit()
    }

    private fun getClientsFromDB() {
        AsyncTask.execute {
            val clients = dbService.getArchivalClients()
            clientsList.clear()
            clientsList.addAll(clients)
            activity?.runOnUiThread {
                clientsListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteArchivalClient(position: Int, id: Long) {
        val builder = AlertDialog.Builder(this.context)

        val clientName = clientsListAdapter.data[position].name
        val message = SpannedText.getSpannedText(getString(R.string.delete_client, clientName))

        builder.setTitle(R.string.warning)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.delete) { dialog, which ->

            builder.setTitle(R.string.deleting_client)
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
            val ret = dbService.deleteArchivalClient(clientsList[position])
            getClientsFromDB()
            if (!ret){
                activity!!.runOnUiThread {
                    toastMessage("Nie można usunąć klienta z Archiwum, gdy nie został on całkowicie zarchiwizowany!")
                }
            }
        }

    }

    private fun openArchivalClientCasesFragment(clientPosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ArchivalCasesFragment(clientsList[clientPosition])
        )?.commit()
    }

    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, message, duration)
        toast.show()
    }
}