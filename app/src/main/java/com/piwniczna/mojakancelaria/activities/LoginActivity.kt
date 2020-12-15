package com.piwniczna.mojakancelaria.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.*
import com.piwniczna.mojakancelaria.R
import java.lang.Exception
import java.lang.NullPointerException
import java.math.BigDecimal
import java.nio.charset.StandardCharsets
import java.time.LocalDate

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
                dbService.initDB()
            }
            catch (e: Exception) {
                Log.e("InitDB", "Root user, case and obligation already in DB")
                e.printStackTrace()
            }

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


    fun initDB() {




    }


}