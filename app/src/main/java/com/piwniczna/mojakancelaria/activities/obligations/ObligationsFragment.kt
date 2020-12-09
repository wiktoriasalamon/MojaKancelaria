package com.piwniczna.mojakancelaria.activities.clients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.client_details.ClientDetailsFragment
import java.math.BigDecimal
import java.time.LocalDate
import kotlin.collections.ArrayList


class ObligationsFragment(var client: ClientEntity)  : Fragment() {
    lateinit var obligationsListView: ListView
    lateinit var obligationsList: ArrayList<ObligationEntity>
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_obligations, container, false)
        dbService = DataService(this.context!!)

        val addButton = view.findViewById<Button>(R.id.add_obligation_button)
        addButton.setOnClickListener { handleAddObligation(it) }

        obligationsListView = view.findViewById(R.id.obligations_list_view) as ListView
        obligationsList = arrayListOf()
        //obligationsListAdapter = ObligationsListAdapter(this.context!!, obligationsList)
        //obligationsListView.adapter = obligationsListAdapter

        obligationsListView.setOnItemClickListener { _, _, position, _ ->
            openObligationDetailsFragment(position)
        }

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
        val ob2 = ObligationEntity(1, ObligationType.HEARING, "Rozprawa 12.12.20", BigDecimal(1000), BigDecimal(0), LocalDate.now(), LocalDate.of(2021,2,23))
        obligationsList.add(ob1)
        obligationsList.add(ob2)
        //obligationsListView.adapter.notifyDataSetChanged()
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

}
