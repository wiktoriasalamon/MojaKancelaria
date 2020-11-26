package com.piwniczna.mojakancelaria.activities.add_client

import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment
import kotlinx.android.synthetic.main.fragment_add_client.*

class ClientDetailsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_client_details, container, false)
        return view
    }

    fun onBackPressed() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.fragment_container,
            ClientsFragment()
        )?.commit()
    }
}