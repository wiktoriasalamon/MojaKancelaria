package com.piwniczna.mojakancelaria.trackingmore

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mashape.unirest.http.HttpResponse
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import com.piwniczna.mojakancelaria.activities.other.letters.Letter
import java.lang.Exception

import java.net.ConnectException
import java.net.URI
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class APIService {
    companion object{
        fun getLetter(num: String): Letter{

            try {

                val url = "https://www.tracktry.com/tracking.php?tracknumber=$num&express=poczta-polska&validate=dxx"
                val response =
                    Unirest
                        .get(url)
                        .header("Content-Type", "application/json")
                        .asString()

                var gsonObj = JsonParser().parse(response.body).asJsonObject

                val data = gsonObj
                var status = ""
                try {
                    status = data["status"].asString
                    if(status=="notfound"){
                        Log.e("kupa","")
                        return Letter(
                            num,
                            TrackingStatus.UNKNOWN_CARRIER,
                            TrackingStatus.UNKNOWN_DAYS)
                    }
                }
                catch (e: Exception){}

                //get all track infos
                try {
                    var info = gsonObj["trackinfo"].asJsonArray
                    var dates = arrayListOf<LocalDate>()
                    var statuses = arrayListOf<String>()

                    for(i in info){
                        val ob = i.asJsonObject
                        statuses.add(ob["StatusDescription"].asString)
                        dates.add(LocalDate.parse(ob["Date"].asString.substring(0,10)))
                    }

                    for(s in statuses){
                        if(s.contains("returned to sender")){
                            return Letter(
                                num,
                                TrackingStatus.RETURNED,
                                0.toString())
                        }
                    }
                    if(statuses[0].contains("Collected") || statuses[0].contains("delivered")){
                        return Letter(
                            num,
                            "${TrackingStatus.DELIVERED}  ${dates[0].format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}",
                            "-")
                    }
                    for(s in statuses.zip(dates)){
                        if(s.first.contains("Postal notice")){
                            val date = s.second.plusDays(14)
                            val days_left = LocalDate.now().until(date,ChronoUnit.DAYS)

                            return Letter(
                                num,
                                TrackingStatus.PICKUP,
                                days_left.toString())
                        }
                    }
                    return Letter(
                        num,
                        TrackingStatus.TRANSIT,
                        "-")

                }catch (e: Exception){
                    e.printStackTrace()
                    return Letter(
                        num,
                        TrackingStatus.UNKNOWN_CARRIER,
                        TrackingStatus.UNKNOWN_DAYS)
                }

                // -> if returned? 20901030157245


            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            throw ConnectException("Cannot connect")

        }


    }
}