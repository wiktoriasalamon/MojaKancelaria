package com.piwniczna.mojakancelaria.activities.client_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment
import com.piwniczna.mojakancelaria.activities.clients.ObligationsFragment

class ClientDetailsFragment(var client: ClientEntity) : Fragment() {
    lateinit var titleTextView: TextView
    lateinit var obligationsButton: Button
    lateinit var paymentsButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_client_details, container, false)

        titleTextView = view.findViewById(R.id.client_details_title)
        titleTextView.text = client.name

        obligationsButton = view.findViewById(R.id.obligations_button)
        obligationsButton.setOnClickListener { openObligationsFragment(it) }

        paymentsButton = view.findViewById(R.id.payments_button)
        paymentsButton.setOnClickListener { openPaymentsFragment(it) }

        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientsFragment()
        )?.commit()
    }

    private fun openObligationsFragment(view: View) {
        fragmentManager?.beginTransaction()?.replace(
                R.id.fragment_container,
                ObligationsFragment(client)
        )?.commit()
    }

    private fun openPaymentsFragment(view: View) {
        //TODO open payments fragment with client as a parameter
        Log.e("DETAILS", "payments")
    }
}