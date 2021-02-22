
package com.piwniczna.mojakancelaria.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "letters")

data class LetterEntity(
    @ColumnInfo(name = "Number")
    var number: String,
    @ColumnInfo(name = "Outgoing")
    var outgoing: Boolean,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0


)