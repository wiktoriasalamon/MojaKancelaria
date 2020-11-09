package com.piwniczna.mojakancelaria.Models

import java.math.BigDecimal
import java.time.LocalDate

class Obligation{

    private var id: Long
    private var type: ObligationType
    private var name: String
    private var amount: BigDecimal
    private var payed: BigDecimal
    private var payments: ArrayList<Payment>
    private var date: LocalDate
    private var paymentDate: LocalDate

    constructor(
            id: Long,
            type: ObligationType,
            name: String,
            amount: BigDecimal,
            payed: BigDecimal,
            payments: ArrayList<Payment>,
            date: LocalDate,
            paymentDate: LocalDate
    ) {
        this.id = id
        this.type = type
        this.name = name
        this.amount = amount
        this.payed = payed
        this.payments = payments
        this.date = date
        this.paymentDate = paymentDate
    }

    fun getId(): Long {
        return this.id
    }

    fun getType(): ObligationType {
        return this.type
    }

    fun getName(): String {
        return this.name
    }

    fun getAmount(): BigDecimal {
        return this.amount
    }

    fun getPayed(): BigDecimal {
        return this.payed
    }

    fun getPayments(): ArrayList<Payment>{
        return this.payments
    }

    fun getDate(): LocalDate {
        return this.date
    }

    fun getPaymentDate(): LocalDate {
        return this.paymentDate
    }

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