
package com.piwniczna.mojakancelaria.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.activities.add_client.AddClientFragment
import com.piwniczna.mojakancelaria.activities.add_obligation.AddObligationFragment
import com.piwniczna.mojakancelaria.activities.client_details.ClientDetailsFragment
import com.piwniczna.mojakancelaria.activities.clients.ClientsFragment
import com.piwniczna.mojakancelaria.activities.clients.ObligationsFragment
import com.piwniczna.mojakancelaria.activities.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.activities.update_obligation.UpdateObligationFragment
import com.piwniczna.mojakancelaria.payments.PaymentsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                is ClientDetailsFragment -> f.onBackPressed()
                is ObligationsFragment -> f.onBackPressed()
                is ObligationDetailsFragment -> f.onBackPressed()
                is AddObligationFragment -> f.onBackPressed()
                is UpdateObligationFragment -> f.onBackPressed()
                is PaymentsFragment -> f.onBackPressed()
                else -> super.onBackPressed()
            }
        }
    }

}
