package com.piwniczna.mojakancelaria.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.piwniczna.mojakancelaria.R

class LoginActivity : AppCompatActivity() {
    lateinit var passwordEditText : EditText
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        passwordEditText = findViewById(R.id.passwordCodeEditText)
        loginButton = findViewById(R.id.loginButton)
    }

    fun handleLogin(view: View) {
        // Log.e("Login","Jestem")
        // TODO: check password
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 123)
    }
}