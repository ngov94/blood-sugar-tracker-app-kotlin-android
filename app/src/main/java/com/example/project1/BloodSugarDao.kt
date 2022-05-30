package com.example.project1

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BloodSugarDao {

    @Insert
    fun insertBloodSugar(bsugar: BloodSugar)

    @Query("select * from bloodsugar")
    fun selectAllBloodSugar(): LiveData<List<BloodSugar>>

    @Query("select * from bloodsugar where bsid=:bsid")
    fun selectBloodSugar(bsid: Int): LiveData<BloodSugar>

    // for daily average statistics
    @Query("select date as day, AVG(sugarConc) as dailyAvg from bloodsugar group by date order by date")
    fun selectDailyAvg(): LiveData<List<DailyAvgStat>>

    @Update
    fun updateBloodSugar(bsugar: BloodSugar)

    @Delete
    fun deleteBloodSugar(bsugar: BloodSugar)

}