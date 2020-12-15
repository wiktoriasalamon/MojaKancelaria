package com.piwniczna.mojakancelaria.DB

import androidx.room.*
import com.piwniczna.mojakancelaria.Models.*

@Dao
interface DAO {
    //inserts
    @Insert
    fun addClient(case: ClientEntity)

    @Insert
    fun addCase(case: CaseEntity)

    @Insert
    fun addObligation(obligation: ObligationEntity)

    @Insert
    fun addPayment(payment: PaymentEntity)

    @Insert
    fun addRelation(relation: RelationEntity)





    //clear all
    @Query("DELETE FROM cases")
    fun clearCases()

    @Query("DELETE FROM clients")
    fun clearClients()

    @Query("DELETE FROM obligations")
    fun clearObligations()

    @Query("DELETE FROM payments")
    fun clearPayments()

    @Query("DELETE FROM relations")
    fun clearRelations()


    //clear by id
    @Query("DELETE FROM clients WHERE id = :clientId")
    fun clearClient(clientId: Int)

    @Query("DELETE FROM cases WHERE id = :caseId")
    fun clearCase(caseId: Int)

    @Query("DELETE FROM obligations WHERE CaseID = :caseId")
    fun clearObligations(caseId: Int)

    @Query("DELETE FROM payments WHERE ClientID = :clientId")
    fun clearPayments(clientId: Int)

    @Query("DELETE FROM relations WHERE ClientID = :clientId")
    fun clearRelations(clientId: Int)


    //get
    @Query("SELECT * FROM clients WHERE id = :id LIMIT 1")
    fun getClient(id: Int): ClientEntity

    @Query("SELECT * FROM clients WHERE (id % 2) = 0")
    fun getClients(): List<ClientEntity>

    @Query("SELECT * FROM clients WHERE (id % 2) = 1 AND id IN (SELECT DISTINCT clientId from CASES )")
    fun getArchivalClients(): List<ClientEntity>

    @Query("SELECT * FROM cases WHERE id = :id LIMIT 1")
    fun getCase(id: Int): CaseEntity

    @Query("SELECT * FROM cases WHERE ClientId = :clientId")
    fun getCases(clientId: Int): List<CaseEntity>

    @Query("SELECT * FROM obligations WHERE CaseId = :caseId")
    fun getObligations(caseId: Int): List<ObligationEntity>

    @Query("SELECT * FROM obligations WHERE id = :obligationId")
    fun getObligation(obligationId: Int): ObligationEntity

    @Query("SELECT * FROM payments WHERE id = :paymentId LIMIT 1")
    fun getPayment(paymentId: Int): PaymentEntity

    @Query("SELECT * FROM payments WHERE id IN (SELECT PaymentId FROM relations WHERE ObligationId IN (SELECT id FROM obligations WHERE CaseId = :caseId))")
    fun getCasePayments(caseId: Int): List<PaymentEntity>

    @Query("SELECT * FROM relations WHERE ObligationId IN (SELECT id FROM obligations WHERE CaseId = :caseId)")
    fun getCaseRelations(caseId: Int): List<RelationEntity>

    @Query("SELECT * FROM payments WHERE ClientId = :clientId")
    fun getPayments(clientId: Int): List<PaymentEntity>

    @Query("SELECT * FROM payments ORDER BY id DESC LIMIT 1")
    fun getLastPayment(): PaymentEntity

    @Query("SELECT * FROM relations WHERE ClientID = :clientId")
    fun getRelations(clientId: Int): List<RelationEntity>

    @Query("SELECT * FROM relations WHERE PaymentId = :paymentId")
    fun getRelationsForPayment(paymentId: Int): List<RelationEntity>

    @Query("SELECT * FROM relations WHERE ObligationId = :obligationId")
    fun getRelationsForObligation(obligationId: Int): List<RelationEntity>

    @Query("SELECT * FROM obligations WHERE id in (:paymentIds)")
    fun getPaymentObligations(paymentIds: Array<Int>): List<ObligationEntity>

    @Query("SELECT * FROM obligations WHERE ClientId = :clientId AND Amount != Payed" )
    fun getNotPayedObligations(clientId: Int): List<ObligationEntity>


    //deletes
    @Delete
    fun deletePayment(payment: PaymentEntity)

    @Delete
    fun deleteObligation(obligation: ObligationEntity)

    @Delete
    fun deleteCase(case: CaseEntity)

    @Delete
    fun deleteClient(client: ClientEntity)


    //updates
    @Update
    fun updateObligations(obligations: List<ObligationEntity>)

    @Update
    fun updateObligation(obligation: ObligationEntity)

    @Update
    fun updateRelations(relations: List<RelationEntity>)

    @Update
    fun updateRelation(relation: RelationEntity)

    @Update
    fun updateClient(client: ClientEntity)

    @Update
    fun updateCase(case: CaseEntity)

    @Update
    fun updatePayment(payment: PaymentEntity)

}