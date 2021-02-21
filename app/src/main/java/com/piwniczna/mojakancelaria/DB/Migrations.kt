package com.piwniczna.mojakancelaria.DB

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrations {
    companion object{
        val MIGRATION_12_13 = object : Migration(12,13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE 'backups' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , 'Date' TEXT)")
            }

        }
    }
}