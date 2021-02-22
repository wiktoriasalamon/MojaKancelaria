package com.piwniczna.mojakancelaria.activities.other


import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ToolsFragment
import java.lang.Exception

class SettingsFragment() : ToolsFragment() {

    lateinit var dbService: DataService
    lateinit var actionBarTitle: String

    lateinit var emailEditText: EditText
    lateinit var autoBackupsEditText: EditText
    lateinit var deleteBackupsEditText: EditText
    lateinit var saveSettingsButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        dbService = DataService(this.context!!)

        emailEditText = view.findViewById(R.id.email_editText)
        autoBackupsEditText = view.findViewById(R.id.auto_backups_editText)
        deleteBackupsEditText = view.findViewById(R.id.delete_backups_editText)
        saveSettingsButton = view.findViewById(R.id.save_settings_button)

        saveSettingsButton.setOnClickListener { saveSettings(it) }

        actionBarTitle = setActionbar()
        loadData()
        return view
    }

    private fun loadData() {
        AsyncTask.execute{
            val email = (dbService.getConstant("default_email"))
            val autoBackup = (dbService.getConstant("auto_backup"))
            val deleteBackup = (dbService.getConstant("delete_backup"))

            activity?.runOnUiThread {
                emailEditText.setText(email)
                autoBackupsEditText.setText(autoBackup)
                deleteBackupsEditText.setText(deleteBackup)
            }
        }


    }

    private fun saveSettings(view: View?) {
        if(!validateData()){
            return
        }
        AsyncTask.execute {
            try {
                dbService.updateConstant("auto_backup", autoBackupsEditText.text.toString().toInt())
                dbService.updateConstant("delete_backup", deleteBackupsEditText.text.toString().toInt())
                dbService.updateConstant("default_email", emailEditText.text.toString())

                activity?.runOnUiThread { toastMessage("Zapisano ustawienia") }
            }
            catch (e: Exception){
                activity?.runOnUiThread { toastMessage("Błąd zapisu ustawień...") }
            }
        }


    }

    private fun validateData(): Boolean {
        val temp = android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()
        if(!temp && emailEditText.text.toString()!=""){
            toastMessage("Błędny email...")
            return false
        }
        if(autoBackupsEditText.text.toString()==""){ autoBackupsEditText.setText("0") }
        if(deleteBackupsEditText.text.toString()==""){ deleteBackupsEditText.setText("0") }
        try{
            autoBackupsEditText.text.toString().toInt()
            deleteBackupsEditText.text.toString().toInt()
        }
        catch (e: NumberFormatException){
            toastMessage("Błędne dane liczbowe")
            return false
        }

        return true
    }

    fun onBackPressed() {
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = actionBarTitle

        fragmentManager?.popBackStack()
    }

    private fun setActionbar(): String {
        val bar = (activity as AppCompatActivity).supportActionBar
        val oldTittle = bar!!.title.toString()
        bar.title = "Ustawienia"
        return oldTittle
    }

    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, message, duration)
        toast.show()
    }



}