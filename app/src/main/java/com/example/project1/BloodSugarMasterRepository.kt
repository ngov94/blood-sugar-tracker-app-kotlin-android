package com.example.project1

import android.content.Context
import androidx.lifecycle.LiveData

class BloodSugarMasterRepository(context: Context) {

    var db: BloodSugarMasterDao? = AppDatabase.getInstance(context)?.bsMasterDao()

    fun selectAllBloodSugarMaster(): LiveData<List<BSMaster>>?{
        return db?.selectAllBloodSugarMaster()
    }

}