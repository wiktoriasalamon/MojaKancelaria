package com.piwniczna.mojakancelaria.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity

class EmailSender {

    companion object {
        fun sendEmail(context: Context, html: String, message: String, email: String) {


            Log.i("Send email", "")
            val TO = arrayOf(email)
            val CC = arrayOf("")
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
            emailIntent.putExtra(Intent.EXTRA_CC, CC)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test mail")
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)
            try {
                startActivity(context, Intent.createChooser(emailIntent, "Wysyłam maila..."),null)
                Log.i("Wysyłanie...", "")
            } catch (ex: ActivityNotFoundException) {
                Log.i("Error...", "")
            }
        }

    }
}

