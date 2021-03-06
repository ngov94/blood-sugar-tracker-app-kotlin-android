package com.example.project1.data

import android.content.Context
import androidx.lifecycle.LiveData

class BloodSugarMasterRepository(context: Context) {

    var db: BloodSugarMasterDao? = AppDatabase.getInstance(context)?.bsMasterDao()

    // Pagination
    fun selectAllBloodSugarMasterPage(offset: Int): LiveData<List<BSMaster>>?{
        return db?.selectAllBloodSugarMasterPage(offset)
    }
    // For total number of records in the list
    fun selectTotalBloodSugarMaster(): LiveData<Int>?{
        return db?.selectTotalBloodSugarMaster()
    }

    // FOR SORTING
    // New to Old
    fun selectAllBloodSugarMasterNewtoOld(offset: Int): LiveData<List<BSMaster>>?{
        return db?.selectAllBloodSugarMasterNewtoOld(offset)
    }

    // Old to New
    fun selectAllBloodSugarMasterOldtoNew(offset: Int): LiveData<List<BSMaster>>?{
        return db?.selectAllBloodSugarMasterOldtoNew(offset)
    }

    // High to Low
    fun selectAllBloodSugarMasterHightoLow(offset: Int): LiveData<List<BSMaster>>?{
        return db?.selectAllBloodSugarMasterHightoLow(offset)
    }

    // Low to High
    fun selectAllBloodSugarMasterLowtoHigh(offset: Int): LiveData<List<BSMaster>>?{
        return db?.selectAllBloodSugarMasterLowtoHigh(offset)
    }

    // FOR SEARCH
    // By Notes
    fun selectAllBSMNotes(searchnotes: String): LiveData<List<BSMaster>>?{
        val search = db?.selectAllBSMNotes(searchnotes)
        if (search != null) {
            search.value
        }
        return search
    }

}