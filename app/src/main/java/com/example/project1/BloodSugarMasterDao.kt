package com.example.project1

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import java.sql.Date

@Dao
interface BloodSugarMasterDao {
//    @Query("select bsid, sugarConc, measured, date, time, notes, sugarUnit, tarRangeLow, tarRangeHigh, hypo, hyper, bloodsugar.mid from bloodsugar inner join master on bloodsugar.mid = master.mid")
//    fun selectAllBloodSugarMaster(): LiveData<List<BSMaster>>

    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid")
    fun selectAllBloodSugarMaster(): LiveData<List<BSMaster>>

    // Pagination
    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid limit 5 offset :offset")
    fun selectAllBloodSugarMasterPage(offset: Int): LiveData<List<BSMaster>>

    @Query("select count(bsid) from bloodsugar inner join master on bloodsugar.mid = master.mid")
    fun selectTotalBloodSugarMaster(): LiveData<Int>

    // FOR SORTING
    // New to Old
    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid order by date desc, time desc limit 5 offset :offset")
    fun selectAllBloodSugarMasterNewtoOld(offset: Int): LiveData<List<BSMaster>>

    // Old to New
    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid order by date, time limit 5 offset :offset")
    fun selectAllBloodSugarMasterOldtoNew(offset: Int): LiveData<List<BSMaster>>

    // High to Low
    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid order by sugarConc desc limit 5 offset :offset")
    fun selectAllBloodSugarMasterHightoLow(offset: Int): LiveData<List<BSMaster>>

    // Low to High
    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid order by sugarConc limit 5 offset :offset")
    fun selectAllBloodSugarMasterLowtoHigh(offset: Int): LiveData<List<BSMaster>>

    // FOR SEARCH
//    // By Date
//    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid where date=:searchdate")
//    fun selectAllBSMDate(searchdate: Date): LiveData<List<BSMaster>>

    // By Notes
    @Query("select * from bloodsugar inner join master on bloodsugar.mid = master.mid where upper(notes) like :searchnotes")
    fun selectAllBSMNotes(searchnotes: String): LiveData<List<BSMaster>>

}