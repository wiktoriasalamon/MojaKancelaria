package com.piwniczna.mojakancelaria.activities.cases

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
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.SpannedText
import com.piwniczna.mojakancelaria.activities.cases.add_case.AddCaseFragment
import com.piwniczna.mojakancelaria.activities.cases.case_details.CaseDetailsFragment
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesListAdapter


class CaseFragment : Fragment() {
    lateinit var casesListAdapter: CasesListAdapter
    lateinit var casesListView : ListView
    lateinit var casesList: ArrayList<CaseEntity>
    lateinit var searchCasesEditText: EditText
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cases, container, false)
        dbService = DataService(this.context!!)

        val addButton = view.findViewById<Button>(R.id.add_case_button)
        addButton.setOnClickListener { handleAddCase(it) }

        casesListView = view.findViewById(R.id.cases_list_view) as ListView
        casesList = arrayListOf()
        casesListAdapter = CasesListAdapter(this.context!!, casesList)
        casesListView.adapter = casesListAdapter

        casesListView.setOnItemClickListener { _, _, position, _ ->
            openCaseDetailsFragment(position)
        }

        casesListView.setOnItemLongClickListener { _, _, position, id ->
            deleteCase(position, id)
            true
        }

        searchCasesEditText = view.findViewById(R.id.search_cases_edittext)
        searchCasesEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                casesListAdapter.filter.filter(s)
            }
        })

        getCasesFromDB()

        return view
    }

    fun onBackPressed() {
        this.activity?.finish()
    }

    private fun getCasesFromDB() {
        AsyncTask.execute {
            val cases = dbService.getCases()
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
        val message = SpannedText.getSpannedText(getString(R.string.delete_case, caseName))

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
                AddCaseFragment()
        )?.commit()
    }

    private fun openCaseDetailsFragment(casePosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            CaseDetailsFragment(casesList[casePosition])
        )?.commit()
    }

}
