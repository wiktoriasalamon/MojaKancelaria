package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.math.BigDecimal


@Entity(tableName = "relations", foreignKeys =
        [
            ForeignKey(entity = CaseEntity::class,
                    parentColumns = arrayOf("id"),
                    childColumns = arrayOf("CaseId"),
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
    @ColumnInfo(name = "CaseId")
    var caseId: Int,
    @ColumnInfo(name = "ObligationId")
    var obligationId: Int,
    @ColumnInfo(name = "PaymentId")
    var paymentId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)