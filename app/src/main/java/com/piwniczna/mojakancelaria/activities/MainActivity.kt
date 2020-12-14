
package com.piwniczna.mojakancelaria.activities

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.appcompat.app.AppCompatActivity
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.archives.cases.ArchivalCaseDetailsFragment
import com.piwniczna.mojakancelaria.activities.archives.cases.ArchivalCasesFragment
import com.piwniczna.mojakancelaria.activities.archives.clients.ArchivalClientsFragment
import com.piwniczna.mojakancelaria.activities.archives.obligations.ArchivalObligationsDetailsFragment
import com.piwniczna.mojakancelaria.activities.archives.obligations.ArchivalObligationsFragment
import com.piwniczna.mojakancelaria.activities.archives.payments.ArchivalPaymentDetailsFragment
import com.piwniczna.mojakancelaria.activities.archives.payments.ArchivalPaymentsFragment
import com.piwniczna.mojakancelaria.activities.cases.ObligationsFragment
import com.piwniczna.mojakancelaria.activities.cases.add_case.AddCaseFragment
import com.piwniczna.mojakancelaria.activities.cases.case_details.CaseDetailsFragment
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment
import com.piwniczna.mojakancelaria.activities.clients.add_client.AddClientFragment
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.activities.obligations.add_obligation.AddObligationFragment
import com.piwniczna.mojakancelaria.activities.obligations.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.activities.obligations.update_obligation.UpdateObligationFragment
import com.piwniczna.mojakancelaria.activities.payments.add_payment.AddPaymentFragment
import com.piwniczna.mojakancelaria.activities.payments.payment_details.PaymentDetailsFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                ClientsFragment()
        ).commit()


    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.fragments
        for (f in fragments) {
            if (f == null) { continue }
            when (f) {
                is ClientsFragment -> f.onBackPressed()
                is AddClientFragment -> f.onBackPressed()
                is CasesFragment -> f.onBackPressed()
                is AddCaseFragment -> f.onBackPressed()
                is CaseDetailsFragment -> f.onBackPressed()
                is ObligationsFragment -> f.onBackPressed()
                is ObligationDetailsFragment -> f.onBackPressed()
                is AddObligationFragment -> f.onBackPressed()
                is UpdateObligationFragment -> f.onBackPressed()
                is AddPaymentFragment -> f.onBackPressed()
                is PaymentsFragment -> f.onBackPressed()
                is PaymentDetailsFragment -> f.onBackPressed()
                is ArchivalClientsFragment -> f.onBackPressed()
                is ArchivalCasesFragment -> f.onBackPressed()
                is ArchivalCaseDetailsFragment -> f.onBackPressed()
                is ArchivalObligationsFragment -> f.onBackPressed()
                is ArchivalObligationsDetailsFragment -> f.onBackPressed()
                is ArchivalPaymentsFragment -> f.onBackPressed()
                is ArchivalPaymentDetailsFragment -> f.onBackPressed()
                else -> super.onBackPressed()
            }
        }
    }



}
