package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "passwords")
data class PasswordEntity(
    @ColumnInfo(name = "hash")
    var hash: String,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

)