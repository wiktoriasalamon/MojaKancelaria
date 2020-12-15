package com.piwniczna.mojakancelaria.DB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.piwniczna.mojakancelaria.Models.*

@Database (
        entities =
        [
            (ClientEntity::class),
            (CaseEntity::class),
            (ObligationEntity::class),
            (PaymentEntity::class),
            (RelationEntity::class)
        ],
        version = 12)
@TypeConverters(Converter::class)

abstract class MyDb : RoomDatabase() {

    abstract fun dao(): DAO
}