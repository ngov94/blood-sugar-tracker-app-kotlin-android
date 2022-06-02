package com.example.project1.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date
import java.sql.Time


@Entity(tableName = "bloodsugar", foreignKeys = [ForeignKey(entity = Master::class, parentColumns = ["mid"], childColumns = ["mid"])])
data class BloodSugar(@PrimaryKey(autoGenerate = true) var bsid: Int? = null,
                      var sugarConc: Double,
                      var measured: String,
                      var date: Date,
                      var time: Time,
                      var notes: String,
                      var mid: Int
                      )


@Entity(tableName = "master")
data class Master(@PrimaryKey(autoGenerate = true) var mid: Int? = null,
                  var sugarUnit: String,
                  var tarRangeLow: Double,
                  var tarRangeHigh: Double,
                  var hypo: Double,
                  var hyper: Double
                  )

data class DailyAvgStat(var day: Date, var dailyAvg: Double)

data class BSMaster(var bsid: Int,
                    var sugarConc: Double,
                    var measured: String,
                    var date: Date,
                    var time: Time,
                    var notes: String,
                    var mid: Int,
                    var sugarUnit: String,
                    var tarRangeLow: Double,
                    var tarRangeHigh: Double,
                    var hypo: Double,
                    var hyper: Double)

