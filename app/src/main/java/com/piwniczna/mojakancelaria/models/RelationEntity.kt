
package com.piwniczna.mojakancelaria.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(tableName = "relations", foreignKeys =
[
    ForeignKey(entity = ClientEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("ClientId"),
            onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = ObligationEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("ObligationId"),
            onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = PaymentEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("PaymentId"),
            onDelete = ForeignKey.CASCADE)
])
data class RelationEntity(


        @ColumnInfo(name = "Amount")
        var amount: BigDecimal,
        @ColumnInfo(name = "ClientId")
        var clientId: Int,
        @ColumnInfo(name = "ObligationId")
        var obligationId: Int,
        @ColumnInfo(name = "PaymentId")
        var paymentId: Int,
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0
)
