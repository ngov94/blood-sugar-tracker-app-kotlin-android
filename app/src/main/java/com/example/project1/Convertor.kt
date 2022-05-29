package com.example.project1

import androidx.room.TypeConverter
import java.sql.Date
import java.sql.Time

object Convertor {

    @TypeConverter
    @JvmStatic
    fun TimestampToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    @JvmStatic
    fun TimestampToTime(value: Long?): Time? {
        return value?.let { Time(it) }
    }

    @TypeConverter
    @JvmStatic
    fun TimeToTimestamp(time: Time?): Long? {
        return time?.time?.toLong()
    }

}