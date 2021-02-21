package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "backups")

data class BackupEntity(
    @ColumnInfo(name = "Date")
    var date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)