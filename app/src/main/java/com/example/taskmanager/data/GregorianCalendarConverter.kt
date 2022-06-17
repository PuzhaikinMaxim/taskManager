package com.example.taskmanager.data

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class GregorianCalendarConverter {

    @TypeConverter
    fun fromGregorianCalendar(gCalendar: GregorianCalendar): Long {
        return gCalendar.timeInMillis
    }

    @TypeConverter
    fun toGregorianCalendar(long: Long): GregorianCalendar {
        val date = GregorianCalendar()
        date.timeInMillis = long
        return date
    }
}