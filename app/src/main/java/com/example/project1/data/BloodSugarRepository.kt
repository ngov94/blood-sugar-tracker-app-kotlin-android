package com.example.project1.data

import android.content.Context
import androidx.lifecycle.LiveData

class BloodSugarRepository(context: Context) {

    var db: BloodSugarDao? = AppDatabase.getInstance(context)?.bloodSugarDao()

    fun insertBloodSugar(bsugar: BloodSugar){
        db?.insertBloodSugar(bsugar)
    }

    fun selectAllBloodSugar(): LiveData<List<BloodSugar>>?{
        return db?.selectAllBloodSugar()
    }

    fun selectDailyAvg(): LiveData<List<DailyAvgStat>>?{
        return db?.selectDailyAvg()
    }

    fun updateBloodSugar(bsugar: BloodSugar){
        db?.updateBloodSugar(bsugar)
    }

    fun deleteBloodSugar(bsugar: BloodSugar){
        db?.deleteBloodSugar(bsugar)
    }
}