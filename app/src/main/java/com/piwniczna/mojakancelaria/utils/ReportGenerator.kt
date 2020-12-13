package com.piwniczna.mojakancelaria.utils

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.widget.Toast
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import com.piwniczna.mojakancelaria.Models.ObligationEntity
import com.piwniczna.mojakancelaria.Models.ObligationType
import com.piwniczna.mojakancelaria.R
import java.math.BigDecimal
import java.time.LocalDate

class ReportGenerator {

    companion object {

        lateinit var dbService: DataService
        lateinit var case: CaseEntity
        lateinit var context: Context
        lateinit var client: ClientEntity


        fun generateReport(case: CaseEntity, context: Context): ArrayList<String> {
            this.dbService = DataService(context)
            this.case = case
            this.context = context
            this.client = dbService.getClient(Companion.case.clientId)

            var html = this.getTableObligations()
            var text = this.getObligations()

            return arrayListOf(html,text)
        }

        private fun getTableObligations(): String {

            val obligationsList = dbService.getObligations(case.id)
            var toReturn =""

            toReturn += "<html>\n" +
                    "    <head>\n" +
                    "    <style>\n" +
                    "    table {\n" +
                    "      font-family: arial, sans-serif;\n" +
                    "      border-collapse: collapse;\n" +
                    "      width: 80%;\n" +
                    "    }\n" +
                    "    \n" +
                    "    td, th {\n" +
                    "      border: 1px solid #dddddd;\n" +
                    "      text-align: left;\n" +
                    "      padding: 8px;\n" +
                    "    }\n" +
                    "    \n" +
                    "    tr:nth-child(even) {\n" +
                    "      background-color: #e0e0e0;\n" +
                    "    }\n" +
                    "    tr:nth-child(odd) {\n" +
                    "      background-color: #f0f0f0;\n" +
                    "    }\n" +
                    "    h3 {\n" +
                    "      font-family: arial, sans-serif;\n" +
                    "    }\n" +
                    "    </style>\n" +
                    "    </head>\n" +
                    "    <body>"

            toReturn += "<h1>Raport</h1><h2>${client.name}: ${case.name}</h2>"
            for(o in obligationsList){
                toReturn += "<br/><h3>${o.name} (${ObligationHelper.getTypeString(o.type, context)}) - ${o.amount.setScale(2)} zł - ${o.convertDate()}</h3>"
                val relationsList = dbService.getRelations(o)
                val paymentsList = dbService.getPayments(relationsList)
                var table = "<table>"
                for(i in relationsList.zip(paymentsList)){
                    val name = i.second.name
                    val amount = i.first.amount.setScale(2).toString() + " zł"
                    val date = i.second.convertDate()
                    table += "<tr><td>${name}</td><td>${amount}</td><td>${date}</td></tr>"
                }
                table += "</table>"
                toReturn += table
            }
            return toReturn + "</body></html>"
        }

        private fun getObligations(): String {
            val obligationsList = dbService.getObligations(case.id)
            var toReturn = "Raport\n${client.name}: ${case.name}\n--------------------------------------------------"
            for(o in obligationsList){
                toReturn += "\n\n${o.name} (${ObligationHelper.getTypeString(o.type, context)}) - ${o.amount.setScale(2)} zł - ${o.convertDate()}"
                val relationsList = dbService.getRelations(o)
                val paymentsList = dbService.getPayments(relationsList)
                for(i in relationsList.zip(paymentsList)){
                    val name = i.second.name
                    val amount = i.first.amount.setScale(2).toString() + " zł"
                    val date = i.second.convertDate()
                    toReturn += "\n\t* $name - $amount - $date"
                }
            }
            return toReturn
        }


    }
}

