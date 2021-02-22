
package com.piwniczna.mojakancelaria.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "constants")

data class ConstantsEntity(
    @PrimaryKey()
    var key: String,
    @ColumnInfo(name = "Value")
    var value: String

)