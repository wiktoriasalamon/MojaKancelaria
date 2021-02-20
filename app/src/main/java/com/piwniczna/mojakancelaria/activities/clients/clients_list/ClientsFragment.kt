package com.piwniczna.mojakancelaria.activities.clients.clients_list

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.clients.ArchivalClientsFragment
import com.piwniczna.mojakancelaria.activities.clients.add_client.AddClientFragment
import com.piwniczna.mojakancelaria.activities.clients.client_details.ClientDetailsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText

class ClientsFragment: Fragment() {
    lateinit var clientsListAdapter: ClientsListAdapter
    lateinit var clientsListView : ListView
    lateinit var clientsList: ArrayList<ClientEntity>
    lateinit var searchClientsEditText: EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_clients, container, false)
        dbService = DataService(this.context!!)

        val addButton = view.findViewById<ImageButton>(R.id.add_client_button)
        addButton.setOnClickListener { handleAddClient(it) }


        clientsListView = view.findViewById(R.id.clients_list_view) as ListView
        clientsList = arrayListOf()
        clientsListAdapter = ClientsListAdapter(this.context!!, clientsList)
        clientsListView.adapter = clientsListAdapter


        clientsListView.setOnItemClickListener { _, _, position, id ->
            Log.e("LISTA", id.toString())
            openClientDetailsFragment(id)
        }

        clientsListView.setOnItemLongClickListener { _, _, position, id ->
            archiveClient(id)
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
        bar!!.setBackgroundDrawable(ColorDrawable(this.context!!.resources.getColor(R.color.dark_blue)))
        bar.title = getString(R.string.app_name)
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

    private fun archiveClient(id: Long) {
        val item = clientsList.filter { it.id == id.toInt() }[0]
        val position = clientsList.indexOf(item)

        AsyncTask.execute {
            var client = clientsListAdapter.data[position]
            val casesList = dbService.getCases(client)
            client.id += 1
            casesList.addAll(dbService.getCases(client))
            client.id -=1

            if (casesList.size == 0){
                activity?.runOnUiThread {
                    val builder = AlertDialog.Builder(this.context)
                    val clientName = clientsListAdapter.data[position].name
                    val message =
                        SpannedText.getSpannedText(getString(R.string.delete_client, clientName))

                    builder.setTitle(R.string.warning)
                    builder.setMessage(message)

                    builder.setPositiveButton("Usuń") { dialog, which ->
                        deleteClient(client)

                    }

                    builder.setNegativeButton(R.string.cancel) { dialog, which -> }

                    builder.show()
                }

            }
            else {
                activity?.runOnUiThread {
                    val builder = AlertDialog.Builder(this.context)
                    val clientName = clientsListAdapter.data[position].name
                    val message =
                        SpannedText.getSpannedText(getString(R.string.archive_client, clientName))

                    builder.setTitle(R.string.warning)
                    builder.setMessage(message)

                    builder.setPositiveButton("Przenieś") { dialog, which ->

                        builder.setTitle("Przenoszenie klienta do archiwum")
                        builder.setMessage(R.string.are_you_sure)

                        builder.setPositiveButton(R.string.yes) { dialog, which -> moveClientToArchive(position)
                        }

                        builder.setNegativeButton(R.string.no) { dialog, which -> }

                        builder.show()

                    }

                    builder.setNegativeButton(R.string.cancel) { dialog, which -> }

                    builder.show()
                }
            }
        }

    }

    private fun moveClientToArchive(position: Int) {
        AsyncTask.execute {
            dbService.deleteClient(clientsList[position])
            getClientsFromDB()
        }
    }

    private fun deleteClient(client: ClientEntity){
        AsyncTask.execute {
            dbService.deleteClient(client)
            client.id += 1
            dbService.deleteArchivalClient(client)
            getClientsFromDB()
        }
    }

    private fun handleAddClient(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                AddClientFragment()
        )?.commit()
    }

    private fun handleOpenArchives(view: View) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ArchivalClientsFragment()
        )?.commit()
    }

    private fun openClientDetailsFragment(id: Long) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ClientDetailsFragment(clientsList.filter { it.id == id.toInt()}[0])
        )?.commit()
    }

    fun onBackPressed() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.warning)
        builder.setMessage("Czy chcesz wyjść z aplikacji?")

        builder.setPositiveButton("Tak") { dialog, which -> exit() }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()

    }

    fun exit() {
        this.activity?.finishAffinity()
    }
}
