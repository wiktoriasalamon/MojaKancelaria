package com.piwniczna.mojakancelaria.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.Toast
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import java.math.BigDecimal
import java.time.LocalDate

class Validator {
    companion object {
       fun validateAmount(amount: String, context: Context, applicationContext: Context?, showToast: Boolean): Boolean {
           if (amount != "" && BigDecimal(amount).compareTo(BigDecimal(0))==1 ){
              return true
           }
           val text = context.resources.getString(R.string.wrong_amount)
           val duration = Toast.LENGTH_LONG
           val toast = Toast.makeText(applicationContext, text, duration)
           Log.e("VALIDATOR", amount)
           if (showToast) {
               toast.show()
           }
           return false
       }
    }
}

