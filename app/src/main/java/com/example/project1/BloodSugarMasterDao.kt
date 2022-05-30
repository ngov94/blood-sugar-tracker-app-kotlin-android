package com.example.project1

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface BloodSugarMasterDao {
//    @Query("select bsid, sugarConc, measured, date, time, notes, sugarUnit, tarRangeLow, tarRangeHigh, hypo, hyper, bloodsugar.mid from bloodsugar inner join master on bloodsugar.mid = master.mid")
//    fun selectAllBloodSugarMaster(): LiveData<List<BSMaster>>

    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid")
    fun selectAllBloodSugarMaster(): LiveData<List<BSMaster>>
}