package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(

    @ColumnInfo(name = "Name")
    var name: String ="",
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)