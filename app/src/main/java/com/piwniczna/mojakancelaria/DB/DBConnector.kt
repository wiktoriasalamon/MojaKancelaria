
package com.piwniczna.mojakancelaria.DB

import android.content.Context
import androidx.room.Room

class DBConnector {
    companion object {
        fun getDB(context: Context): MyDb {
            lateinit var database: MyDb
            try {
                database = Room.databaseBuilder(
                        context,
                        MyDb::class.java,
                        "kancelaria"
                ).addMigrations(Migrations.MIGRATION_12_13).build()
            } catch (e: Exception) {
            }

            return database
        }
    }
}