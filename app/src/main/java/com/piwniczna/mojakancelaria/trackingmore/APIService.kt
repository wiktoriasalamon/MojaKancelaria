package com.piwniczna.mojakancelaria.trackingmore

import com.google.gson.JsonObject
import com.mashape.unirest.http.Unirest
import com.piwniczna.mojakancelaria.activities.other.letters.Letter
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.URI


class APIService {
    companion object{
        fun getLettes(numbers: ArrayList<String>): ArrayList<Letter>{

            if(numbers.isEmpty()){
                return arrayListOf<Letter>()
            }
            var numbersString = ""
            for(n in numbers){
                numbersString += "$n,"
            }
            numbersString = numbersString.substring(0, numbersString.lastIndex-1)

            for(i in 1..10) {
                val response = Unirest
                    .get("https://api.trackingmore.com/v2/trackings/get?numbers=$numbersString")
                    .header("Content-Type", "application/json")
                    .header("Trackingmore-Api-Key", ApiKeys.getApiKey())
                    .asString()

                var json = JSONParser().parse(response.body) as JSONObject

                val meta = json.get("meta") as JSONObject
                if(meta.get("code") as Int != 200 ){
                    continue
                }

                json = json.get("data") as JSONObject
                //iter on array of items

                //check carrer


                //sleep i seconds
            }
            //todo: return false - sth wrong with network






            TODO()
        }


    }
}