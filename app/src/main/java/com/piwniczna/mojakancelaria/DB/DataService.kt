package com.piwniczna.mojakancelaria.DB

import android.content.Context
import android.util.Log
import com.piwniczna.mojakancelaria.Models.*
import java.math.BigDecimal
import java.time.LocalDate

class DataService(context: Context) {
    var db: DAO = DBConnector.getDB(context).dao()



    //cases
    fun addCase(case: CaseEntity){
        db.addCase(case)
    }

    fun deleteCase(case: CaseEntity){
        db.deleteCase(case)
    }

    fun getCase(caseId: Int): CaseEntity {
        return db.getCase(caseId)
    }

    fun getCases(client: ClientEntity): ArrayList<CaseEntity> {
        return ArrayList(db.getCases(client.id))
    }

    fun setCaseArchival(case: CaseEntity) {
        val obligations = db.getObligations(case.id)
        case.clientId += 1 //change client to archival version of client
        db.updateCase(case)
        for (o in obligations) {
            o.clientId += 1
            updateObligation(o)
        }
    }

    //clients
    fun addClient(client: ClientEntity){
        db.addClient(client)
        db.addClient(client) //to make archival version of client
    }

    fun initDB(){
        db.addClient(ClientEntity("root", 1))
        db.addCase(CaseEntity(1, "root_case", 1))
        db.addObligation(ObligationEntity(1, 1, ObligationType.CONTRACT, "Usunięte zobowiązanie", BigDecimal.ZERO, BigDecimal.ZERO, LocalDate.now(), LocalDate.now(), 1))

    }

    fun deleteClient(client: ClientEntity): Boolean{
        if(client.id % 2 == 1) {
            return false //activeClient has even id
        }
        val payments = db.getPayments(client.id)
        val cases = db.getCases(client.id)
        val relations = db.getRelations(client.id)

        for (c in cases) {
            setCaseArchival(c)
        }

        for (p in payments) {
            p.clientId += 1
            db.updatePayment(p)
        }

        for (r in relations) {
            r.clientId += 1
            db.updateRelation(r)
        }

        db.deleteClient(client)
        return true
    }

    fun deleteArchivalClient(client: ClientEntity): Boolean {
        if(client.id % 2 == 0) {
            return false //archivalClient has odd id
        }
        val activeClient = getClient(client.id - 1)
        if (activeClient != null) {
            return false //cannot delete archivalClient if activeClient exists
        }
        db.deleteClient(client)
        return true
    }

    fun getClients(): ArrayList<ClientEntity> {
        return ArrayList(db.getClients())
    }

    fun getArchivalClients(): ArrayList<ClientEntity> {
        val toReturn = ArrayList(db.getArchivalClients())
        toReturn.remove(ClientEntity("root",1)) //root client has also odd id
        return toReturn
    }

    fun getClient(clientId: Int): ClientEntity {
        return db.getClient(clientId)
    }

    fun ifClientExists(clientName: String): Boolean {
        if(db.getClient(clientName) != null) {
            return true
        }
        return false
    }


    //obligations
    fun addObligation(obligation: ObligationEntity){
        db.addObligation(obligation)
    }

    fun getObligations(caseId: Int) : ArrayList<ObligationEntity> {
        return ArrayList(db.getObligations(caseId))
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
            r.obligationId = 1
        }
        db.updateRelations(relations)

        return db.deleteObligation(obligation)
    }

    fun getSumOfObligationsAmountsToPay(case: CaseEntity): BigDecimal {
        val obligations = ArrayList(db.getObligations(case.id))
        var sum = BigDecimal.ZERO
        var leftToPay : BigDecimal
        for (o in obligations) {
            leftToPay = o.amount - o.payed
            sum += leftToPay
        }
        return sum
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

    fun getPayments(caseId: Int) : ArrayList<PaymentEntity> {
        return ArrayList(db.getPayments(caseId))
    }

    fun getPayments(relations: ArrayList<RelationEntity>) : ArrayList<PaymentEntity> {
        var listToRet = arrayListOf<PaymentEntity>()
        for(r in relations){
            listToRet.add(db.getPayment(r.paymentId))
        }
        return listToRet
    }


    //relations
    fun getRelations(caseId: Int) : ArrayList<RelationEntity> {
        return ArrayList(db.getRelations(caseId))
    }

    fun getRelations(payment: PaymentEntity) : ArrayList<RelationEntity> {
        return ArrayList(db.getRelationsForPayment(payment.id))
    }

    fun getRelations(obligation: ObligationEntity) : ArrayList<RelationEntity> {
        return ArrayList(db.getRelationsForObligation(obligation.id))
    }
}