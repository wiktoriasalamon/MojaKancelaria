package com.piwniczna.mojakancelaria.activities.clients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.client_details.ClientDetailsFragment
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

        val addButton = view.findViewById<Button>(R.id.add_obligation_button)
        addButton.setOnClickListener { handleAddObligation(it) }

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

        val contractTypeButton = view.findViewById(R.id.contract_type_button) as ToggleButton
        configureToggleButton(contractTypeButton, ObligationType.CONTRACT)

        val stampTypeButton = view.findViewById(R.id.stamp_type_button) as ToggleButton
        configureToggleButton(stampTypeButton, ObligationType.STAMP)

        val courtTypeButton = view.findViewById(R.id.court_type_button) as ToggleButton
        configureToggleButton(courtTypeButton, ObligationType.COURT)

        val hearingTypeButton = view.findViewById(R.id.hearing_type_button) as ToggleButton
        configureToggleButton(hearingTypeButton, ObligationType.HEARING)

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
        val ob1 = ObligationEntity(1, ObligationType.STAMP, "list nr 2", BigDecimal(10), BigDecimal(0), LocalDate.now(), LocalDate.of(2021,2,23))
        val ob2 = ObligationEntity(1, ObligationType.HEARING, "Rozprawa 12.12.20", BigDecimal(1000), BigDecimal(50), LocalDate.now(), LocalDate.of(2021,2,23))
        val ob3 = ObligationEntity(1, ObligationType.COURT, "Rozprawa", BigDecimal(1000), BigDecimal(1000), LocalDate.now(), LocalDate.of(2021,2,23))
        val ob4 = ObligationEntity(1, ObligationType.CONTRACT, "Umowa 1", BigDecimal(1000), BigDecimal(50), LocalDate.now(), LocalDate.of(2020,2,23))
        obligationsList.addAll(arrayListOf(ob1, ob2, ob3, ob4))
        obligationsListAdapter.notifyDataSetChanged()
        /*AsyncTask.execute {
            val obligations = dbService.getObligations()
            obligationsList.clear()
            obligationsList.addAll(obligations)
            activity?.runOnUiThread {
                obligationsListAdapter.notifyDataSetChanged()
            }
        }*/
    }


    private fun handleAddObligation(view: View) {
        //TODO: open add obligation fragment
        Log.e("OBLIGATIONS", "add new obligation")
    }

    private fun openObligationDetailsFragment(obligationPosition: Int) {
        //TODO: go to obligations details fragment
        /*fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ObligationDetailsFragment(obligationsList[obligationPosition])
        )?.commit()*/
        Log.e("OBLIGATIONS", "obligation details")
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
