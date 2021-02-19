package com.piwniczna.mojakancelaria.utils

import android.content.Context
import java.math.BigDecimal

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

