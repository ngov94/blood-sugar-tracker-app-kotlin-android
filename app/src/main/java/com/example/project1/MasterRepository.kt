package com.example.project1

import android.content.Context
import androidx.lifecycle.LiveData


class MasterRepository(context: Context) {

    var db: MasterDao? = AppDatabase.getInstance(context)?.masterDao()

    fun insertMaster(master: Master){
        db?.insertMaster(master)
    }

    fun selectLatestMaster(): LiveData<Master>?{
        return db?.selectLatestMaster()
    }

    fun selectMasterItem(mid: Int): LiveData<Master>?{
        return db?.selectMasterItem(mid)
    }
}