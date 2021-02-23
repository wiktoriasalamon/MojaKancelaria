package com.piwniczna.mojakancelaria.trackingmore

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mashape.unirest.http.Unirest
import com.piwniczna.mojakancelaria.activities.other.letters.Letter

import java.net.ConnectException
import java.net.URI


class APIService {
    companion object{
        fun getLetters(num: List<String>): ArrayList<Letter>{
            var letters = arrayListOf<Letter>()
            var numbers = ArrayList(num.distinct())
            if(numbers.isEmpty()){
                return letters
            }

            var numbersString = ""
            for(n in numbers){
                numbersString += "$n,"
            }

            for(i in 1..10) {
                letters.clear()

                val response = Unirest.get("https://api.trackingmore.com/v2/trackings/get?numbers=$numbersString")
                    .header("Content-Type", "application/json")
                    .header("Trackingmore-Api-Key", ApiKeys.getApiKey()).asString()


                var gsonObj = JsonParser().parse(response.body).asJsonObject
                //var json = JSONParser().parse(response.body) as JSONObject

                val meta = gsonObj["meta"].asJsonObject
                //val meta = json["meta"] as JSONObject
                if(meta["code"].asInt != 200 ){
                    Thread.sleep(1000)
                    continue
                }

                val arr = gsonObj["data"].asJsonObject["items"].asJsonArray

                // j - single package
                Log.e("len ",arr.size().toString())
                for(j in arr){
                    if(j.asJsonObject["carrier_code"].asString != "poczta-polska"){
                        letters.add(
                            Letter(
                                j.asJsonObject["tracking_number"].asString,
                                TrackingStatus.UNKNOWN_CARRIER,
                                "-")
                        )
                        continue
                        //todo Check for single letter
                    }
                    when (j.asJsonObject["status"].asString) {
                        //TODO()
                        "delivered" -> {
                            letters.add(
                                Letter(
                                    j.asJsonObject["tracking_number"].asString,
                                    TrackingStatus.DELIVERED,
                                    "-")
                            )
                        }
                        "pickup" -> {
                            letters.add(
                                Letter(
                                    j.asJsonObject["tracking_number"].asString,
                                    TrackingStatus.PICKUP,
                                    "-")
                            )
                        }
                        "transit" -> {
                            letters.add(
                                Letter(
                                    j.asJsonObject["tracking_number"].asString,
                                    TrackingStatus.TRANSIT,
                                    "-")
                            )
                        }
                        "expired" -> {
                            letters.add(
                                Letter(
                                    j.asJsonObject["tracking_number"].asString,
                                    TrackingStatus.EXPIRED,
                                    "-")
                            )
                        }
                        "undelivered" -> {
                            letters.add(
                                Letter(
                                    j.asJsonObject["tracking_number"].asString,
                                    TrackingStatus.UNDELIVERED,
                                    "-")
                            )
                        }
                        else -> {
                            letters.add(
                                Letter(
                                    j.asJsonObject["tracking_number"].asString,
                                    j.asJsonObject["status"].asString+" else",
                                    "-")
                            )
                        }
                    }
                    //TODO()
                }
                //TODO() Check empty letters and append as broken
                Log.e("Pobrano dane","----")
                return letters


            }

            throw ConnectException("Cannot connect ")

        }


    }
}