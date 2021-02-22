
package com.piwniczna.mojakancelaria.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "constants")

data class ConstantsEntity(
    @PrimaryKey()
    var key: String,
    @ColumnInfo(name = "Value")
    var value: String

)