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


    //login
    @Insert
    fun addHash(password: PasswordEntity)

    @Query("DELETE FROM passwords")
    fun delHash()

    @Query("SELECT * FROM passwords LIMIT 1")
    fun getHash(): PasswordEntity


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
    @Query("SELECT * FROM clients")
    fun getClients(): List<ClientEntity>

    @Query("SELECT * FROM cases")
    fun getCases(): List<CaseEntity>

    @Query("SELECT * FROM obligations WHERE CaseId = :caseId")
    fun getObligations(caseId: Int): List<ObligationEntity>

    @Query("SELECT * FROM obligations WHERE id = :obligationId")
    fun getObligation(obligationId: Int): ObligationEntity

    @Query("SELECT * FROM payments WHERE ClientID = :clientId")
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

    @Query("SELECT * FROM obligations WHERE CaseId = :caseId AND Amount != Payed" )
    fun getNotPayedObligations(caseId: Int): List<ObligationEntity>


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
    fun updateClient(client: ClientEntity)

}