package com.piwniczna.mojakancelaria.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.piwniczna.mojakancelaria.Models.*

@Database (
        entities =
        [
            (CaseEntity::class),
            (ObligationEntity::class),
            (PaymentEntity::class),
            (RelationEntity::class),
            (PasswordEntity::class)
        ],
        version = 7)
@TypeConverters(Converter::class)

abstract class MyDb : RoomDatabase() {

    abstract fun dao(): DAO
}