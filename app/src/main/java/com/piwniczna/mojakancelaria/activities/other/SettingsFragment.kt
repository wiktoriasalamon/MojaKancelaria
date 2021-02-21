package com.piwniczna.mojakancelaria.activities.other


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
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
import com.piwniczna.mojakancelaria.DB.MyBackup
import com.piwniczna.mojakancelaria.DB.MyDb
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.utils.ToolsFragment
import ir.androidexception.roomdatabasebackupandrestore.Restore
import java.io.File

//TODO
class SettingsFragment() : ToolsFragment() {

    lateinit var dbService: DataService
    lateinit var actionBarTitle: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        dbService = DataService(this.context!!)

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



    private fun toastMessage(message: String) {
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(activity?.applicationContext, message, duration)
        toast.show()
    }



}