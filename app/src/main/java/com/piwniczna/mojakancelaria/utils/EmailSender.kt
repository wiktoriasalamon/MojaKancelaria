package com.piwniczna.mojakancelaria.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity

class EmailSender {

    companion object {
        fun sendEmail(context: Context, uri: Uri, message: String, email: String) {


            Log.i("Send email", "")
            val TO = arrayOf(email)
            val CC = arrayOf("")
            val emailIntent = Intent(Intent.ACTION_SEND)
                .setData(Uri.parse("mailto:"))
                .setType("text/plain")
                .putExtra(Intent.EXTRA_EMAIL, TO)
                .putExtra(Intent.EXTRA_CC, CC)
                .putExtra(Intent.EXTRA_SUBJECT, "Raport z aplikacji Moja Kancelaria")
                .putExtra(Intent.EXTRA_TEXT, message)
                .putExtra(Intent.EXTRA_STREAM, uri)
            try {
                startActivity(context, Intent.createChooser(emailIntent, "Wysyłam maila..."),null)
                Log.i("Wysyłanie...", "")
            } catch (ex: ActivityNotFoundException) {
                Log.i("Error...", "")
            }
        }

    }
}

