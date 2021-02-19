package com.piwniczna.mojakancelaria.activities.settings


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.DB.MyDb
import com.piwniczna.mojakancelaria.R
import ir.androidexception.roomdatabasebackupandrestore.Backup
import ir.androidexception.roomdatabasebackupandrestore.Restore
import java.io.File


class SettingsFragment() : Fragment() {
    lateinit var backupButton: Button
    lateinit var restoreButton: Button
    lateinit var infoText: TextView
    lateinit var dbService: DataService
    lateinit var actionBarTitle: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.settings, container, false)
        dbService = DataService(this.context!!)

        backupButton = view.findViewById(R.id.backup_button)
        backupButton.setOnClickListener { backupAction(it) }

        restoreButton = view.findViewById(R.id.restore_button)
        restoreButton.setOnClickListener { restoreAction(it) }

        infoText = view.findViewById(R.id.info_text)

        infoText.text = (Html.fromHtml(getString(R.string.about_app_description)))
        actionBarTitle = setActionbar()

        return view
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

    private fun backupAction(view: View) {
        val dialog = Dialog(this.context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_dialog_get_backup_name)

        val confirmButton = dialog.findViewById(R.id.confirm_backup) as Button
        val backupNameEditText = dialog.findViewById(R.id.backup_name_edit_text) as EditText

        confirmButton.setOnClickListener {


             val imm = dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             imm.hideSoftInputFromWindow(view.windowToken, 0)

             val name = backupNameEditText.text.toString()
             if(validName(name)){
                 if(backupDB(name)){
                     toastMessage("Wykonano backup!")
                     dialog.dismiss()
                 }
                 else{
                     toastMessage("Niepowodzenie...")
                     dialog.dismiss()
                 }
             }
             else{
                 toastMessage("Podaj popraną nazwę - tylko litery cyfry i podkreślnik")
             }

         }
        dialog.show()

    }


    private fun restoreAction(view: View) {
        val dialog = Dialog(this.context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_dialog_restore)

        val confirmButton = dialog.findViewById(R.id.confirm_backup) as Button
        val restoreSpinner = dialog.findViewById(R.id.restore_spinner) as Spinner

        val files = (File(context?.getExternalFilesDir(null).toString()+"/backup/").listFiles())
        val names = files!!
            .filter { it.name.endsWith(".db.bkp") }
            .map { it.name.substring(0,it.name.length-7) }


        setSpinner(restoreSpinner, names)

        confirmButton.setOnClickListener {


            val imm = dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            val position = restoreSpinner.selectedItemPosition
            if(position != 0){
                //toastMessage(names[position-1])
                restoreDB(names[position-1])
            }
            else{
                toastMessage("Wybierz plik!")
            }

        }
        dialog.show()

    }

    private fun setSpinner(restoreSpinner: Spinner, names: List<String>){
        val spinnerList = arrayListOf<String>()
        spinnerList.add("-")
        spinnerList.addAll(names)

        val aa = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, spinnerList)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(restoreSpinner)
        {
            adapter = aa
            setSelection(0, false)
            gravity = android.view.Gravity.CENTER
        }
    }

    private fun restoreDB(name: String): Boolean{
        """Log.e("","Restore")
        Restore.Init()
            .database(database)
            .backupFilePath("path-to-backup-file/filename.txt")
            .secretKey("your-secret-key") // if your backup file is encrypted, this parameter is required
            .onWorkFinishListener { success, message ->
                // do anything
            }
            .execute()"""
        return false
    }

    private fun backupDB(name: String): Boolean{
        var toReturn = false
        val db = Room.databaseBuilder(
            context!!,
            MyDb::class.java,
            "kancelaria"
        ).build()
        val location = context?.getExternalFilesDir(null).toString()

        Backup.Init()
            .database(db)
            .path(location + "/backup/")
            .fileName("$name.db.bkp")
            .onWorkFinishListener { success, message ->
                toReturn = success
            }
            .execute()
        return toReturn
    }

    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, message, duration)
        toast.show()
    }

    private fun validName(name: String) : Boolean{
        val regex = """[a-zA-Z][a-zA-Z0-9_]{3,14}""".toRegex()
        return regex.matches(name)
    }

}