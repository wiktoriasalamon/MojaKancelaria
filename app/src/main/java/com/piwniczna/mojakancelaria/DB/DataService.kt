package com.piwniczna.mojakancelaria.DB

import android.content.Context
import com.piwniczna.mojakancelaria.Models.*
import java.math.BigDecimal

class DataService(context: Context) {
    var db: DAO = DBConnector.getDB(context).dao()

    //password
    fun getPasswordHash(): PasswordEntity{
        return db.getHash()
    }

    fun addNewPassword(password: PasswordEntity){
        db.addHash(password)
    }

    //clients
    fun addClient(client: ClientEntity){
        db.addClient(client)
    }

    fun deleteClient(client: ClientEntity){
        db.deleteClient(client)
    }

    fun getClients(): ArrayList<ClientEntity> {
        return ArrayList(db.getClients())
    }

    //??
/*
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

        for(i in obligationIdList.zip(amountsList)){
            db.addRelation(RelationEntity(i.second,payment.clientId,i.first,payment.id))
        }



        //todo for relation object update obligation
        return true
    }
*/







}