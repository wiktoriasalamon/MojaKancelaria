package com.piwniczna.mojakancelaria.activities.clients

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.add_client.AddClientFragment
import com.piwniczna.mojakancelaria.activities.add_obligation.AddObligationFragment
import com.piwniczna.mojakancelaria.activities.client_details.ClientDetailsFragment
import com.piwniczna.mojakancelaria.activities.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.activities.obligations.ObligationsListAdapter
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.collections.ArrayList


class ObligationsFragment(var client: ClientEntity)  : Fragment() {
    lateinit var obligationsListView: ListView
    lateinit var obligationsList: ArrayList<ObligationEntity>
    lateinit var obligationsListAdapter: ObligationsListAdapter
    lateinit var dbService: DataService
    lateinit var typeFilters: ArrayList<ObligationType>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_obligations, container, false)
        dbService = DataService(this.context!!)

        obligationsListView = view.findViewById(R.id.obligations_list_view) as ListView
        obligationsList = arrayListOf()
        obligationsListAdapter = ObligationsListAdapter(this.context!!, obligationsList)
        obligationsListView.adapter = obligationsListAdapter

        obligationsListView.setOnItemClickListener { _, _, position, _ ->
            openObligationDetailsFragment(position)
        }

        typeFilters = arrayListOf()
        for (i in ObligationType.values()) {
            typeFilters.add(i)
        }

        val addButton = view.findViewById<Button>(R.id.add_obligation_button)
        addButton.setOnClickListener { handleAddObligation(it) }

        val contractTypeButton = view.findViewById(R.id.contract_type_button) as ToggleButton
        configureToggleButton(contractTypeButton, ObligationType.CONTRACT)

        val stampTypeButton = view.findViewById(R.id.stamp_type_button) as ToggleButton
        configureToggleButton(stampTypeButton, ObligationType.STAMP)

        val courtTypeButton = view.findViewById(R.id.court_type_button) as ToggleButton
        configureToggleButton(courtTypeButton, ObligationType.COURT)

        val hearingTypeButton = view.findViewById(R.id.hearing_type_button) as ToggleButton
        configureToggleButton(hearingTypeButton, ObligationType.HEARING)

        val clientNameTextView = view.findViewById<TextView>(R.id.obligations_client_name)
        clientNameTextView.text = client.name

        getObligationsFromDB()

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ClientDetailsFragment(client)
        )?.commit()
    }

    private fun getObligationsFromDB() {
        AsyncTask.execute {
            val obligations = dbService.getObligations(client.id)
            obligationsList.clear()
            obligationsList.addAll(obligations)
            activity?.runOnUiThread {
                obligationsListAdapter.notifyDataSetChanged()
            }
        }
    }


    private fun handleAddObligation(view: View) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            AddObligationFragment()
        )?.commit()
    }

    private fun openObligationDetailsFragment(obligationPosition: Int) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ObligationDetailsFragment(client, obligationsList[obligationPosition])
        )?.commit()
    }

    private fun configureToggleButton(button: ToggleButton, type: ObligationType) {
        button.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                typeFilters.add(type)
                button.backgroundTintList = this.context!!.resources.getColorStateList(R.color.dark_blue)
            } else {
                typeFilters.remove(type)
                button.backgroundTintList = this.context!!.resources.getColorStateList(R.color.grey)
            }
            obligationsListAdapter.filter(typeFilters)
        }
    }

}
