package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cases", foreignKeys = [ForeignKey(entity = ClientEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ClientId"),
        onDelete = ForeignKey.CASCADE)])

data class CaseEntity(
    @ColumnInfo(name = "ClientId")
    var clientId: Int = 0,
    @ColumnInfo(name = "Name")
    var name: String ="",
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)
