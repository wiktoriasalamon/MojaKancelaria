package com.piwniczna.mojakancelaria.activities.clients.client_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Update
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.activities.clients.update_client.UpdateClientFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment


class ClientDetailsFragment(var client: ClientEntity) : Fragment() {
    lateinit var casesButton: Button
    lateinit var paymentsButton: Button
    lateinit var updateNameButton: ImageButton
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_client_details, container, false)
        dbService = DataService(this.context!!)

        casesButton = view.findViewById(R.id.cases_button)
        casesButton.setOnClickListener { openCasesFragment(it) }

        paymentsButton = view.findViewById(R.id.payments_button)
        paymentsButton.setOnClickListener { openPaymentsFragment(it) }

        updateNameButton = view.findViewById(R.id.open_update_client_button)
        updateNameButton.setOnClickListener { openUpdateClientName(it) }
        setActionbar()

        return view
    }

    private fun setActionbar() {
        val bar = (activity as AppCompatActivity).supportActionBar
        bar!!.title = client.name
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ClientsFragment()
        )?.commit()
    }

    private fun openCasesFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                CasesFragment(client)
        )?.commit()
    }

    private fun openPaymentsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                PaymentsFragment(client)
        )?.commit()
    }

    private fun openUpdateClientName(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                UpdateClientFragment(client)
        )?.commit()
    }


}