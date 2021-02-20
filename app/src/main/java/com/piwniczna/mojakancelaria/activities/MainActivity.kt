
package com.piwniczna.mojakancelaria.activities

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
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
import com.piwniczna.mojakancelaria.activities.cases.cases_list.CasesFragment
import com.piwniczna.mojakancelaria.activities.clients.add_client.AddClientFragment
import com.piwniczna.mojakancelaria.activities.clients.client_details.ClientDetailsFragment
import com.piwniczna.mojakancelaria.activities.clients.clients_list.ClientsFragment
import com.piwniczna.mojakancelaria.activities.obligations.add_obligation.AddObligationFragment
import com.piwniczna.mojakancelaria.activities.obligations.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.activities.obligations.update_obligation.UpdateObligationFragment
import com.piwniczna.mojakancelaria.activities.other.BackupFragment
import com.piwniczna.mojakancelaria.activities.payments.add_payment.AddPaymentFragment
import com.piwniczna.mojakancelaria.activities.payments.payment_details.PaymentDetailsFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment
import com.piwniczna.mojakancelaria.activities.other.SettingsFragment
import com.piwniczna.mojakancelaria.utils.ReportGenerator.Companion.context
import com.piwniczna.mojakancelaria.utils.SpannedText
import ir.androidexception.roomdatabasebackupandrestore.Backup


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var drawer: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            ClientsFragment()
        ).commit()

        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_clients -> openFragment(ClientsFragment())
            R.id.nav_archives -> openFragment(ArchivalClientsFragment())
            //TODO:
            //R.id.nav_settings -> openFragment(SettingsFragment(), stack=true)
            R.id.nav_info -> showAboutDialog()
            R.id.nav_exit -> exit()
            R.id.nav_backup -> openFragment(BackupFragment(), stack=true)
        }
        drawer.closeDrawer(GravityCompat.START)


        return true
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
                is ClientDetailsFragment -> f.onBackPressed()
                is SettingsFragment -> f.onBackPressed()
                else -> super.onBackPressed()
            }
        }
    }



    private fun openFragment(classs: Fragment, stack: Boolean =false) {
        val f = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (f!!::class == classs::class) {
            Log.e("Warning", "Already in ${classs::class}")
            return
        }
        if(stack) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                classs
            ).addToBackStack(null).commit()
        }
        else{
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                classs
            ).commit()
        }
    }


    private fun showAboutDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.about_app_title)
        builder.setMessage(SpannedText.getSpannedText(getString(R.string.about_app_description)))

        builder.setPositiveButton("ok") { dialog, which -> }

        builder.show()
    }

    private fun exit(){
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.warning)
        builder.setMessage("Czy chcesz wyjść z aplikacji?")

        builder.setPositiveButton("Tak") { dialog, which -> this.finishAffinity() }

        builder.setNegativeButton(R.string.cancel) { dialog, which -> }

        builder.show()
    }




}
