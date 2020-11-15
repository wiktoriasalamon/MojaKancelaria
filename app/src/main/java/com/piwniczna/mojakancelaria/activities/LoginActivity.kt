package com.piwniczna.mojakancelaria.activities

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.room.Room
import com.piwniczna.mojakancelaria.DB.MyDb
import com.piwniczna.mojakancelaria.R
import com.piwniczna.mojakancelaria.Models.PasswordEntity

class LoginActivity : AppCompatActivity() {
    lateinit var passwordEditText : EditText
    lateinit var loginButton: Button
    lateinit var database : MyDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        passwordEditText = findViewById(R.id.passwordCodeEditText)
        loginButton = findViewById(R.id.loginButton)

        testDb()
    }

    fun handleLogin(view: View) {
        // Log.e("Login","Jestem")
        // TODO: check password
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 123)
    }

    fun testDb(){
        AsyncTask.execute {

            //how to init db
            try {
                database = Room.databaseBuilder(
                    this,
                    MyDb::class.java,
                    "kancelaria"
                ).build()
            } catch (e: Exception) {
            }



            //Usunac po pierwszym dodaniu hasha do bazy
            val password = PasswordEntity("123456")
            database.dao().addHash(password)
            //

            //pobieranie hasha
            val hash = database.dao().getHash()

            //wypisanie
            System.out.println("*************************************************")
            System.out.println(hash)
            System.out.println("*************************************************")


        }


    }
}