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
                        return Letter(
                            num,
                            TrackingStatus.UNKNOWN_CARRIER,
                            TrackingStatus.UNKNOWN_DAYS)
                    }
                }
                catch (e: Exception){}

                //get all track infos
                //if any contains 'returned to sender' -> returned
                //if firs contains 'Collected' or 'delivered'   -> delivered
                //if any contains 'Postal notice' -> pickup, get this records date and count 14 days
                // -> if returned? 20901030157245
                //else transit



                when (status) {
                    //TODO() - wyliczyć dni dla aviza!
                    "delivered" -> {
                        return Letter(
                                num,
                                TrackingStatus.DELIVERED,
                                "-")

                    }
                    "pickup" -> {
                        return Letter(
                                num,
                                TrackingStatus.PICKUP,
                                "-")

                    }
                    "transit" -> {
                        return Letter(
                                num,
                                TrackingStatus.TRANSIT,
                                "-")

                    }


                    else -> {
                        return Letter(
                                num,
                                status,
                                TrackingStatus.UNKNOWN_DAYS)

                    }
                }

            }
            catch (e: Exception) {
                e.printStackTrace()
            }

            throw ConnectException("Cannot connect")

        }


    }
}