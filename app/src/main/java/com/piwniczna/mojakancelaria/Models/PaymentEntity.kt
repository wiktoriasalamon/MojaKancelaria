package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "payments", foreignKeys = [ForeignKey(entity = ClientEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ClientId"),
        onDelete = ForeignKey.CASCADE)])
data class PaymentEntity(


    @ColumnInfo(name = "ClientId")
    var clientId : Int,
    @ColumnInfo(name = "Name")
    var name: String,
    @ColumnInfo(name = "Amount")
    var amount: BigDecimal,
    @ColumnInfo(name = "Date")
    var date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)