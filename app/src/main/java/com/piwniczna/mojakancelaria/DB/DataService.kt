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
        val toReturn = ArrayList(db.getClients())
        toReturn.remove(ClientEntity("root",999999))
        return toReturn
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

    fun getNotPayedObligations(clientId: Int) : ArrayList<ObligationEntity> {
        return ArrayList(db.getNotPayedObligations(clientId))
    }

    fun updateObligation(obligation: ObligationEntity) {
        return db.updateObligation(obligation)
    }

    fun deleteObligation(obligation: ObligationEntity) {
        var relations = db.getRelationsForObligation(obligation.id)
        for(r in relations){
            r.obligationId = 999999
        }
        db.updateRelations(relations)

        return db.deleteObligation(obligation)
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

    fun deletePayment(payment: PaymentEntity){
        val relationsList = db.getRelationsForPayment(payment.id)
        for(r in relationsList){
            var obligation = db.getObligation(r.obligationId)
            obligation.payed = obligation.amount.minus(r.amount)
            db.updateObligation(obligation)
        }
        db.deletePayment(payment)
    }

    fun getPayments(clientId: Int) : ArrayList<PaymentEntity> {
        return ArrayList(db.getPayments(clientId))
    }

    //todo: delete payment!!!!

    //relations
    fun getRelations(clientId: Int) : ArrayList<RelationEntity> {
        return ArrayList(db.getRelations(clientId))
    }

    fun getRelations(payment: PaymentEntity) : ArrayList<RelationEntity> {
        return ArrayList(db.getRelationsForPayment(payment.id))
    }








}