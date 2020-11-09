package com.piwniczna.mojakancelaria.Models

class Client {
    private var id: Long
    private var firstName: String
    private var lastName: String

    constructor(
        id: Long,
        firstName: String,
        lastName: String
    ) {
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
    }

    override fun toString() : String{
        return this.firstName +" "+this.lastName
    }

}