package com.piwniczna.mojakancelaria.activities.archives.cases

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.clients.ArchivalClientsFragment
import com.piwniczna.mojakancelaria.utils.ArchivalFragment
import com.piwniczna.mojakancelaria.utils.SpannedText


class ArchivalCasesFragment(val client: ClientEntity) : ArchivalFragment() {
    lateinit var archivalCasesListAdapter: ArchivalCasesListAdapter
    lateinit var casesListView : ListView
    lateinit var casesList: ArrayList<CaseEntity>
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_archival_cases, container, false)
        dbService = DataService(this.context!!)

        casesListView = view.findViewById(R.id.clients_cases_list_view) as ListView
        casesList = arrayListOf()
        archivalCasesListAdapter = ArchivalCasesListAdapter(this.context!!, casesList, activity!!)
        casesListView.adapter = archivalCasesListAdapter

        casesListView.setOnItemClickListener { _, _, position, _ ->
            openCaseDetailsFragment(position)
        }

        casesListView.setOnItemLongClickListener { _, _, position, id ->
            deleteCase(position, id)
            true
        }

        getCasesFromDB()

        setActionbar()

        return view
    }

    private fun setActionbar() {
        val bar = (activity as AppCompatActivity).supportActionBar
        bar!!.title = getString(R.string.client_archives, client.name)
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ArchivalClientsFragment()
        )?.commit()
    }

    private fun getCasesFromDB() {
        AsyncTask.execute {
            val cases = dbService.getCases(client)
            casesList.clear()
            casesList.addAll(cases)
            activity?.runOnUiThread {
                archivalCasesListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteCase(position: Int, id: Long) {
        val builder = AlertDialog.Builder(this.context)

        val caseName = archivalCasesListAdapter.data[position].name
        val message = SpannedText.getSpannedText(getString(R.string.delete_client_case, caseName, client.name))

        builder.setTitle(R.string.warning)
        builder.setMessage(message)

        builder.setPositiveButton(R.string.delete) { dialog, which ->

            builder.setTitle(R.string.deleting_case)
            builder.setMessage(R.string.are_you_sure)

            builder.setPositiveButton(R.string.yes) { dialog, which -> deleteCaseFromDB(position) }

            builder.setNegativeButton(R.string.no) { dialog, which -> }

            builder.show()

        }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()
    }

    private fun deleteCaseFromDB(position: Int) {
        AsyncTask.execute {
            dbService.deleteCase(casesList[position])
            getCasesFromDB()
        }
    }

    private fun openCaseDetailsFragment(position: Int) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ArchivalCaseDetailsFragment(client, casesList[position])
        )?.commit()
    }

}
