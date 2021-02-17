package com.piwniczna.mojakancelaria.activities.settings


import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment


class SettingsFragment() : Fragment() {
    lateinit var backupButton: Button
    lateinit var restoreButton: Button
    lateinit var infoText: TextView
    lateinit var dbService: DataService

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings, container, false)
        dbService = DataService(this.context!!)

        backupButton = view.findViewById(R.id.backup_button)
        backupButton.setOnClickListener { backupAction(it) }

        restoreButton = view.findViewById(R.id.restore_button)
        restoreButton.setOnClickListener { restoreAction(it) }

        infoText = view.findViewById(R.id.info_text)

        infoText.text = (Html.fromHtml(getString(R.string.about_app_description)))
        return view
    }


    fun onBackPressed() {
        fragmentManager?.popBackStack()
    }

    private fun backupAction(view: View) {
        //todo:
        Log.e("","Backup")
    }

    private fun restoreAction(view: View) {
        //todo:
        Log.e("","Restore")

    }

}