package com.piwniczna.mojakancelaria.DB

import androidx.room.TypeConverter
import com.piwniczna.mojakancelaria.Models.ObligationType
import java.math.BigDecimal
import java.time.LocalDate


class Converter {
    @TypeConverter
    fun fromType(type: ObligationType): String {
        return type.name
    }

    @TypeConverter
    fun toType(type: String): ObligationType {
        return ObligationType.valueOf(type)
    }

    @TypeConverter
    fun fromDecimal(decimal: BigDecimal): String {
        return decimal.toPlainString()
    }

    @TypeConverter
    fun toDecimal(decimal: String): BigDecimal {
        return BigDecimal(decimal)
    }

    @TypeConverter
    fun fromDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }
}