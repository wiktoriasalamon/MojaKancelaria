package com.piwniczna.mojakancelaria.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.common.hash.Hashing
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.*
import com.piwniczna.mojakancelaria.R
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

        passwordEditText = findViewById(R.id.passwordCodeEditText)
        loginButton = findViewById(R.id.loginButton)
        dbService = DataService(this)
    }

    override fun onPause() {
        super.onPause()
        passwordEditText.setText("")
    }

    fun handleLogin(view: View) {
        val pin = passwordEditText.text
        val pinHash = Hashing.sha256()
                .hashString(pin, StandardCharsets.UTF_8)
                .toString()

        AsyncTask.execute {
            try {
                val dbHash = dbService.getPasswordHash()
                if(dbHash.equals(null)){
                    throw NullPointerException()
                }
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
                        val duration = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(applicationContext, text, duration)
                        toast.show()

                        openMainActivity()

                    }
                }
            }
            catch (e: NullPointerException){
                runOnUiThread {
                    showPasswordDialog()
                }
            }

        }
    }

    fun openMainActivity() {
        val myIntent = Intent(this, MainActivity::class.java)
        startActivityForResult(myIntent, 123)
    }

    fun showPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(R.string.new_password_code_hint)
        val dialogLayout = inflater.inflate(R.layout.password_dialog, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.ok) { _, _ ->  setPin(editText.text.toString())}
        builder.show()

    }

    fun setPin(newPin:String) {
        AsyncTask.execute {
            if (newPin.length < 5) {
                runOnUiThread {
                    val text = R.string.weak_pin_toast
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                }
            }
            else {
                val pinHash = Hashing.sha256()
                        .hashString(newPin, StandardCharsets.UTF_8)
                        .toString()
                dbService.addNewPassword(PasswordEntity(pinHash))
                dbService.addClient(ClientEntity("root", 999999))
                dbService.addCase(CaseEntity(999999, "root_case", 999999))
                dbService.addObligation(ObligationEntity(999999, 999999, ObligationType.CONTRACT, "Usunięte zobowiązanie", BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now(), LocalDate.now(), 999999))
            }
        }
    }
}