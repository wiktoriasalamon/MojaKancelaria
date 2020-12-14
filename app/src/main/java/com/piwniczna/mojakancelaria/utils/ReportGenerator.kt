package com.piwniczna.mojakancelaria.utils

import android.content.Context
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.Models.CaseEntity
import com.piwniczna.mojakancelaria.Models.ClientEntity
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

            val html = replacePolishSymbols(getHtmlObligations())
            val text = getObligations()

            return arrayListOf(html,text)
        }

        private fun getHtmlObligations(): String {

            val obligationsList = dbService.getObligations(case.id)
            var summary = BigDecimal.ZERO
            var payed = BigDecimal.ZERO


            for(o in obligationsList){
                summary = summary.add(o.amount)
                payed = payed.add(o.payed)
            }

            var toReturn =""
            toReturn += "<html>\n" +
                    "    <head>\n" +
                    "    <meta charset=\"UTF-8\">"
                    "    <style>\n" +
                    "    table {\n" +
                    "      font-family: arial, sans-serif;\n" +
                    "      border-collapse: collapse;\n" +
                    "      width: 100%;\n" +
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

            toReturn +=
                    "<h1>Podsumowanie na dzien ${DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now())}</h1>" +
                    "<h3>Klient: ${client.name}</br>Sprawa: ${case.name}</h3>" +
                    "<hr><hr>"

            toReturn +=
                    "<h3>Koszt calkowity: ${summary.setScale(2)} zl</br>" +
                    "Oplacono: ${payed.setScale(2)} zl</br>" +
                    "Pozostalo do oplacenia: ${summary.minus(payed).setScale(2)} zl</h3>" +
                    "<hr><hr>"

            for(o in obligationsList){
                toReturn +=
                        "<h4>Zobowiazanie: ${o.name} (${ObligationHelper.getTypeString(o.type, context)})</h4>" +
                        "<p>Termin platnosci: ${o.convertPaymentDate()}</br>" +
                        "Kwota: ${o.amount.setScale(2)} zl</br>" +
                        "Oplacono: ${o.payed.setScale(2)} zl"

                if(o.amount.minus(o.payed).setScale(2).compareTo(BigDecimal.ZERO)!=0) {
                    toReturn += "</br>Pozostalo do oplacenia: ${o.amount.minus(o.payed).setScale(2)}"
                }
                toReturn += "</p>"


                val relationsList = dbService.getRelations(o)
                val paymentsList = dbService.getPayments(relationsList)
                if(relationsList.size==0){
                    toReturn += "<hr>"
                    continue
                }
                var table = "<table>"
                table +=
                        "<tr>" +
                        "    <th>Nazwa wplaty</th>" +
                        "    <th>Kwota</th>" +
                        "    <th>Data platnosci</th>" +
                        "  </tr>"
                for(i in relationsList.zip(paymentsList)){
                    val name = i.second.name
                    val amount = i.first.amount.setScale(2).toString() + " zl"
                    val date = i.second.convertDate()
                    table += "<tr><td>${name}</td><td>${amount}</td><td>${date}</td></tr>"
                }
                table += "</table><hr>"
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

        private fun replacePolishSymbols(text: String) : String {
            var text = text
            val l1 = arrayListOf("ą","ę","ó","ł","ż","ź","ć")
            val l2 = arrayListOf("a","e","o","l","z","z","c")

            for (e in l1.zip(l2)){
                text = text.replace(e.first,e.second)
            }

            text = text.replace("ą","a")
            text = text.replace("ę","e")
            text = text.replace("ó","o")
            text = text.replace("ł","l")
            text = text.replace("ż","z")
            text = text.replace("ź","z")
            text = text.replace("ć","c")

            return text
        }


    }
}

