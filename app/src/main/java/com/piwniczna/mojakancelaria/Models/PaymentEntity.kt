package com.piwniczna.mojakancelaria.Models

import androidx.room.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
) {
    fun convertDate(): String{
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return formatter.format(date)
    }
}

