package com.example.project1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time


@Entity(tableName = "bloodsugar", foreignKeys = [ForeignKey(entity = Master::class, parentColumns = ["mid"], childColumns = ["mid"])])
data class BloodSugar(@PrimaryKey(autoGenerate = true) var bsid: Int? = null,
                      @ColumnInfo(name = "sugar_conc") var sugarConc: Double,
                      var measured: String,
                      var date: Date,
                      var time: Time,
                      var notes: String,
                      var mid: Int
                      )


@Entity(tableName = "master")
data class Master(@PrimaryKey(autoGenerate = true) var mid: Int? = null,
                  @ColumnInfo(name = "sugar_unit") var sugarUnit: String,
                  @ColumnInfo(name = "target_range_low") var tarRangeLow: Double,
                  @ColumnInfo(name = "target_range_high") var tarRangeHigh: Double,
                  var hypo: Double,
                  var hyper: Double,
                  )

data class DailyAvgStat(var day: Date, var dailyAvg: Double)