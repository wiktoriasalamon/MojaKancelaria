
package com.piwniczna.mojakancelaria.activities

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.MenuItem
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.piwniczna.mojakancelaria.DB.DataService
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
import com.piwniczna.mojakancelaria.activities.clients.update_client.UpdateClientFragment
import com.piwniczna.mojakancelaria.activities.obligations.add_obligation.AddObligationFragment
import com.piwniczna.mojakancelaria.activities.obligations.obligation_details.ObligationDetailsFragment
import com.piwniczna.mojakancelaria.activities.obligations.update_obligation.UpdateObligationFragment
import com.piwniczna.mojakancelaria.activities.other.backup.BackupFragment
import com.piwniczna.mojakancelaria.activities.other.letters.LettersFragment
import com.piwniczna.mojakancelaria.activities.payments.add_payment.AddPaymentFragment
import com.piwniczna.mojakancelaria.activities.payments.payment_details.PaymentDetailsFragment
import com.piwniczna.mojakancelaria.activities.payments.payments_list.PaymentsFragment
import com.piwniczna.mojakancelaria.activities.other.settings.SettingsFragment
import com.piwniczna.mojakancelaria.utils.SpannedText


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    lateinit var drawer: DrawerLayout
    lateinit var dbService: DataService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dbService = DataService(this)

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

        setupActionBar()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_clients -> openFragment(ClientsFragment())
            R.id.nav_archives -> openFragment(ArchivalClientsFragment())
            R.id.nav_settings -> openFragment(SettingsFragment(), stack=true)
            R.id.nav_info -> showAboutDialog()
            R.id.nav_exit -> exit()
            R.id.nav_backup -> openFragment(BackupFragment(), stack=true)
            R.id.nav_new_letter -> showLetterDialog()
            R.id.nav_avizo -> openFragment(LettersFragment(false))
            R.id.nav_sent -> openFragment(LettersFragment(true))

        }
        drawer.closeDrawer(GravityCompat.START)


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            //TODO Home button id - dunno why R.id.home wont work...
            16908332 -> {
                drawer.openDrawer(GravityCompat.START)
                return true
            }
        }
        return false
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
                is LettersFragment -> f.onBackPressed()
                is UpdateClientFragment -> f.onBackPressed()
                else -> super.onBackPressed()
            }
        }
    }

    private fun setupActionBar() {
        val bar = (this as AppCompatActivity).supportActionBar
        bar!!.setDisplayHomeAsUpEnabled(true)
        bar.setHomeButtonEnabled(true)
        bar.setHomeAsUpIndicator(R.drawable.ic_home)


    }

    private fun openFragment(fragment: Fragment, stack: Boolean =false) {
        val f = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (f!!::class == fragment::class && fragment::class != LettersFragment::class) {
            Log.e("Warning", "Already in ${fragment::class}")
            return
        }
        if(stack) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                fragment
            ).addToBackStack(null).commit()
        }
        else{
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                fragment
            ).commit()
        }
    }

    private fun showLetterDialog() {
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_dialog_new_avizo)

        val editText = dialog.findViewById<EditText>(R.id.letter_number_editText)
        val outgoingSwitch = dialog.findViewById<Switch>(R.id.outgoing_switch)
        val barcodeButton = dialog.findViewById<ImageButton>(R.id.scan_barcode_button)
        val confirmButton = dialog.findViewById<Button>(R.id.confirm_new_letter)

        barcodeButton.setOnClickListener { toastMessage(getString(R.string.todo))}
        confirmButton.setOnClickListener {
            val number = editText.text.toString()
            val outgoing = outgoingSwitch.isChecked
            if(number==""){
                toastMessage(getString(R.string.empty_letter_num))
            }
            else {
                AsyncTask.execute {
                    dbService.addLetter(number, outgoing)
                    this.runOnUiThread {
                        toastMessage(getString(R.string.added_letter))
                        dialog.dismiss()
                    }
                }

            }
        }


        dialog.show()
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

    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(this, message, duration)
        toast.show()
    }




}
