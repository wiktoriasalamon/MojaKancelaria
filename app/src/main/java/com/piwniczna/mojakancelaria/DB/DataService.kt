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


    //obligations
    fun addObligation(obligation: ObligationEntity){
        db.addObligation(obligation)
    }

    fun getObligations(clientId: Int) : ArrayList<ObligationEntity> {
        return ArrayList(db.getObligations(clientId))
    }

    fun getObligation(obligationId :Int): ObligationEntity {
        return db.getObligation(obligationId)
    }

    //payments
    fun addPayment(payment: PaymentEntity, obligationList: List<ObligationEntity>, amountsList: List<BigDecimal>) : Boolean{
        if(obligationList.size != amountsList.size){
            return false
        }
        if(amountsList.contains(BigDecimal(0))){
            return false
        }

        db.addPayment(payment)
        val addedPayment = db.getLastPayment()

        for(i in obligationList.zip(amountsList)){
            db.addRelation(RelationEntity(
                    amount = i.second,
                    clientId = payment.clientId,
                    obligationId = i.first.id,
                    paymentId = addedPayment.id
            ))
            i.first.payed = i.first.payed.add(i.second)
        }

        db.updateObligations(obligationList)
        return true
    }

    fun getPayments(clientId: Int) : ArrayList<PaymentEntity> {
        return ArrayList(db.getPayments(clientId))
    }

    //relations
    fun getRelations(clientId: Int) : ArrayList<RelationEntity> {
        return ArrayList(db.getRelations(clientId))
    }








}