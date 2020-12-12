package com.piwniczna.mojakancelaria.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.Toast
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import java.math.BigDecimal
import java.time.LocalDate

class RaportGenerator {

    companion object {

        lateinit var dbService: DataService
        lateinit var case: CaseEntity
        lateinit var context: Context

       fun generateRaport(case: CaseEntity, context: Context): String {
           this.dbService = DataService(context)
           this.case = case
           this.context = context
           val payments = getPayments()
           val obligations = getObligations()

           return ""
       }

        private fun getObligations(): String {
            val obligationsList = dbService.getObligations(case.id)
            var toReturn = ""
            for(o in obligationsList){
                toReturn += "\n${o.name}\t-\t${ObligationHelper.getTypeString(o.type, context)}\t\t-\t${o.amount.setScale(2).toString()} zł\t\t-\t${o.date.toString()}"
            }
            return toReturn
        }

        private fun getPayments(): String {
            return ""
        }
    }
}

