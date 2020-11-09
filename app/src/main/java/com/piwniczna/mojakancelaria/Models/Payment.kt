package com.piwniczna.mojakancelaria.Models

import java.math.BigDecimal
import java.time.LocalDate

class Payment {
    private var id: Long
    private var name: String
    private var amount: BigDecimal
    private var date: LocalDate
    private var obligations: ArrayList<Obligation>

    constructor(
        id: Long,
        name: String,
        amount: BigDecimal,
        date: LocalDate,
        obligations: ArrayList<Obligation>
    ) {
        this.id = id
        this.name = name
        this.amount = amount
        this.date = date
        this.obligations = obligations
    }


    fun getName(): String {
        return this.name
    }

    fun getAmount(): BigDecimal {
        return this.amount
    }

    fun getDate(): LocalDate {
        return this.date
    }

    fun getObligations(): ArrayList<Obligation> {
        return this.obligations
    }
}