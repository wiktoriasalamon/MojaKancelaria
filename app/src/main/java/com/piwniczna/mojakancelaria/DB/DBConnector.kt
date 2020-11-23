package com.piwniczna.mojakancelaria.DB

import android.content.Context
import android.os.AsyncTask
import androidx.room.Room
import androidx.room.RoomDatabase

class DBConnector {
    companion object {
        fun getDB(context: Context): MyDb {
            lateinit var database: MyDb

            try {
                database = Room.databaseBuilder(
                        context,
                        MyDb::class.java,
                        "kancelaria"
                ).build()
            } catch (e: Exception) {
            }

            return database
        }
    }

}
