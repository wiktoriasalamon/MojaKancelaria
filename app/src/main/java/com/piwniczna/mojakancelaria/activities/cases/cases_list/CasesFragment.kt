package com.piwniczna.mojakancelaria.activities.cases.cases_list

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.cases.ObligationsFragment
import com.piwniczna.mojakancelaria.activities.cases.add_case.AddCaseFragment
import com.piwniczna.mojakancelaria.activities.clients.client_details.ClientDetailsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText


class CasesFragment(val client: ClientEntity) : Fragment() {
    lateinit var casesListAdapter: CasesListAdapter
    lateinit var casesListView : ListView
    lateinit var casesList: ArrayList<CaseEntity>
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cases, container, false)
        dbService = DataService(this.context!!)

        val addButton = view.findViewById<Button>(R.id.add_case_button)
        addButton.setOnClickListener { handleAddCase(it) }

        casesListView = view.findViewById(R.id.clients_cases_list_view) as ListView
        casesList = arrayListOf()
        casesListAdapter = CasesListAdapter(this.context!!, casesList)
        casesListView.adapter = casesListAdapter

        casesListView.setOnItemClickListener { _, _, position, _ ->
            openObligationsFragment(position)
        }

        casesListView.setOnItemLongClickListener { _, _, position, id ->
            deleteCase(position, id)
            true
        }

        getCasesFromDB()

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ClientDetailsFragment(client)
        )?.commit()
    }

    private fun getCasesFromDB() {
        AsyncTask.execute {
            val cases = dbService.getCases(client)
            casesList.clear()
            casesList.addAll(cases)
            activity?.runOnUiThread {
                casesListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun deleteCase(position: Int, id: Long) {
        val builder = AlertDialog.Builder(this.context)

        val caseName = casesListAdapter.data[position].name
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

    private fun handleAddCase(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                AddCaseFragment(client)
        )?.commit()
    }

    private fun openObligationsFragment(position: Int) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ObligationsFragment(client, casesList[position])
        )?.commit()
    }

}
