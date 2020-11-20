package com.piwniczna.mojakancelaria.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.piwniczna.mojakancelaria.Models.*

@Dao
interface DAO {
    @Insert
    fun addClient(client: ClientEntity)

    @Insert
    fun addObligation(obligation: ObligationEntity)

    @Insert
    fun addPayment(payment: PaymentEntity)

    @Insert
    fun addRelation(relation: RelationEntity)

    @Insert
    fun addHash(password: PasswordEntity)


    @Query("DELETE FROM passwords")
    fun delHash()


    @Query("SELECT * FROM clients")
    fun getClients(): List<ClientEntity>

    @Query("SELECT * FROM passwords LIMIT 1")
    fun getHash(): PasswordEntity



}