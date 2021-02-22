package com.piwniczna.mojakancelaria.backup

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.piwniczna.mojakancelaria.DB.DataService
import com.piwniczna.mojakancelaria.DB.MyDb
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AutoBackup {
    companion object{
        private lateinit var dbService: DataService

        fun autoBackupDB(dbService: DataService, context: Context){
            Companion.dbService = dbService
            try{
                if(backupRequired()){
                    backupDB(context)
                }
                else{
                    Log.e("Auto backup","Not required")
                    return
                }
                Log.e("Auto backup","Done!")
            }
            catch (e: Exception){
                e.printStackTrace()
                Log.e("Auto backup","Error!")
            }
        }

        fun removeBackups(dbService: DataService, context: Context){
            val temp = dbService.getBackupsToDelete(dbService.getConstant("delete_backup").toInt())
            val backups = temp.distinctBy {it.date}

            Log.e("Backups to delete ", backups.size.toString())
            for(b in backups){
                dbService.deleteBackup(b)
                val location = context.getExternalFilesDir(null).toString()
                val fname = "$location/backup/${b.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.!auto.db.bkp"
                Log.e("file: ","$location/backup/${b.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}.!auto.db.bkp")
                Files.deleteIfExists(Paths.get(fname))
            }
        }

        private fun backupRequired(): Boolean{
            val lastBackup = dbService.getLastBackup()
            if(lastBackup == null){
                Log.e("Bkp","No backup in db")
                return true
            }
            val lastBackupDate = lastBackup.date
            val autoBackupDays = (dbService.getConstant("auto_backup")).toLong()


            return lastBackupDate.plusDays(autoBackupDays-1).isBefore(LocalDate.now())
        }

        private fun backupDB(context: Context): Boolean{
            val name = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            var toReturn = false
            val db = Room.databaseBuilder(
                context,
                MyDb::class.java,
                "kancelaria"
            ).build()
            val location = context.getExternalFilesDir(null).toString()

            MyBackup.Init()
                .database(db)
                .path("$location/backup/")
                .fileName("$name.!auto.db.bkp")
                //.secretKey("your-secret-key")
                .onWorkFinishListener { success, message ->
                    toReturn = success
                }
                .execute()

            dbService.addBackup(LocalDate.now())
            return toReturn
        }


    }
}