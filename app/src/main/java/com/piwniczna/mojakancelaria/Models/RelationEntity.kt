package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(tableName = "relations")
data class RelationEntity(


    @ColumnInfo(name = "Amount")
    var amount: BigDecimal,
    @ColumnInfo(name = "ObligationId")
    var obligationId: Int,
    @ColumnInfo(name = "PaymentId")
    var paymentId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)