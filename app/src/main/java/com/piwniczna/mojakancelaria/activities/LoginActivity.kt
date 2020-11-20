package com.piwniczna.mojakancelaria.activities

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.common.hash.Hashing
import com.piwniczna.mojakancelaria.DB.MyDb
import com.piwniczna.mojakancelaria.R
import java.nio.charset.StandardCharsets


class LoginActivity : AppCompatActivity() {
    lateinit var passwordEditText : EditText
    lateinit var loginButton: Button
    lateinit var database : MyDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        passwordEditText = findViewById(R.id.passwordCodeEditText)
        loginButton = findViewById(R.id.loginButton)

        initDb(this)
    }

    fun handleLogin(view: View) {
        val pin = passwordEditText.text
        val pinHash = Hashing.sha256()
                .hashString(pin, StandardCharsets.UTF_8)
                .toString()

        AsyncTask.execute {
            val dbHash = database.dao().getHash()

            runOnUiThread {

                if (!pinHash.equals(dbHash.hash)) {
                    val text = R.string.login_failed_toast
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()

                    passwordEditText.setText("")

                }
                else {
                    val text = R.string.login_successful_toast
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()

                    openMainActivity()

                }
            }
        }
    }

    fun openMainActivity() {
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 123)
    }

    fun initDb(context: Context) {
        AsyncTask.execute {

            try {
                database = Room.databaseBuilder(
                        context,
                        MyDb::class.java,
                        "kancelaria"
                ).build()
            } catch (e: Exception) {
            }
        }
    }
}