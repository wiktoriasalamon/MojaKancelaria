package com.piwniczna.mojakancelaria.DB

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrations {
    companion object{
        val MIGRATION_12_13 = object : Migration(12,13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `backups` (`Date` TEXT NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
            }

        }
        val MIGRATION_13_14 = object : Migration(13,14) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `constants` (`key` TEXT PRIMARY KEY NOT NULL, `Value` TEXT NOT NULL)")
            }

        }

    }
}