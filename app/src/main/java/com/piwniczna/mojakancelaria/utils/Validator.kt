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
       fun validateAmount(amount: String, context: Context, applicationContext: Context?): Boolean {
           if (amount != "" && BigDecimal(amount).compareTo(BigDecimal(0))==1 ){
              return true
           }

           return false
       }
    }
}

