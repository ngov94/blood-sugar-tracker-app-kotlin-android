package com.example.project1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bloodsugar")
data class BloodSugar(@PrimaryKey(autoGenerate = true) var bsid: Int? = null,
                      @ColumnInfo(name = "sugar_conc") var sugarConc: Double,
                      @ColumnInfo(name = "sugar_unit") var sugarUnit: String,
                      @ColumnInfo var measured: String,
                      @ColumnInfo var date: String,
                      @ColumnInfo var time: String,
                      @ColumnInfo var notes: String)
