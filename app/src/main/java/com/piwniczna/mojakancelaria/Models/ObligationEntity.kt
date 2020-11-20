package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate


@Entity(tableName = "obligations")
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


    fun pay(toPay: BigDecimal): Boolean {
        val difference: BigDecimal = this.amount.minus(this.payed)
        if(difference.compareTo(toPay)==-1){
            return false
        }
        this.payed = this.payed.add(toPay)
        return true
    }

    /**
     * returns:
     * 0 if nothing payed
     * 1 if partially payed
     * 2 if already payed
     * 3 if not payed and overdue
     * -1 if error
     */
    fun getStatus(): Int {
        if(this.amount.compareTo(this.payed)==1 && this.paymentDate < LocalDate.now()){
            return 3
        }
        if(this.amount.compareTo(this.payed)==0) {
            return 2
        }
        if(this.payed.compareTo(BigDecimal(0))==1) {
            return 1
        }
        if(this.payed.compareTo(BigDecimal(0))==0) {
            return 0
        }
        return -1
    }
}