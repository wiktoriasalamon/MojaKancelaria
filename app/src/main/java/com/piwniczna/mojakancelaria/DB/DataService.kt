package com.piwniczna.mojakancelaria.DB

import android.content.Context
import com.piwniczna.mojakancelaria.Models.*
import java.math.BigDecimal

class DataService(context: Context) {
    var db: DAO = DBConnector.getDB(context).dao()

    fun getPinHash(): PasswordEntity{
        return db.getHash()
    }

    fun addNewPassword(password: PasswordEntity){
        db.addHash(password)
    }

    fun addClient(client: ClientEntity){
        db.addClient(client)
    }

    fun addObligation(obligation: ObligationEntity){
        db.addObligation(obligation)
    }

    fun addPayment(payment: PaymentEntity, obligationIdList: List<Int>, amountsList: List<BigDecimal>) : Boolean{
        if(obligationIdList.size != amountsList.size){
            return false
        }
        if(amountsList.contains(BigDecimal(0))){
            return false
        }

        db.addPayment(payment)

        val paymentId = payment.id
        val clientId = payment.clientId


        for(i in obligationIdList.zip(amountsList)){
            db.addRelation(RelationEntity(i.second,clientId,i.first,paymentId))
        }


        //todo for relation object update obligation
        return true
    }








}