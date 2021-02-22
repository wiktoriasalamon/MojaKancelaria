package com.piwniczna.mojakancelaria.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.piwniczna.mojakancelaria.backup.AutoBackup
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.R

class LoginActivity : AppCompatActivity() {
    lateinit var passwordEditText : EditText
    lateinit var loginButton: Button
    lateinit var dbService: DataService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.loginButton)
        dbService = DataService(this)
    }


    fun handleLogin(view: View) {
        AsyncTask.execute {
            try {
                dbService.initConstants()
                dbService.initDB()
            }
            catch (e: android.database.sqlite.SQLiteConstraintException) {
                Log.e("InitDB", "Root user, case and obligation already in DB")
            }

            AutoBackup.autoBackupDB(dbService, applicationContext)
            AutoBackup.removeBackups(dbService, applicationContext)


            runOnUiThread{
                Log.e("Welcome",":)")

                openMainActivity()
            }

        }
    }

    fun openMainActivity() {
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 123)
    }

}