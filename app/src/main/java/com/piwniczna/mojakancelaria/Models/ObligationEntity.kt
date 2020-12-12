package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate


@Entity(tableName = "obligations", foreignKeys = [ForeignKey(entity = ClientEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("ClientId"),
        onDelete = ForeignKey.CASCADE)])
data class ObligationEntity(

    @ColumnInfo(name = "ClientId")
    var clientId : Int,
    @ColumnInfo(name = "Type")
    var type: ObligationType,
    @ColumnInfo(name = "Name")
    var name: String,
    @ColumnInfo(name = "Amount")
    var amount: BigDecimal,
    @ColumnInfo(name = "Payed")
    var payed: BigDecimal,
    @ColumnInfo(name = "Date")
    var date: LocalDate,
    @ColumnInfo(name = "PaymentDate")
    var paymentDate: LocalDate,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {

}