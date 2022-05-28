package com.example.project1

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface MasterDao {
    @Insert
    fun insertMaster(master: Master)

    @Query("select * from master order by mid desc limit 1")
    fun selectLatestMaster(): LiveData<Master>

    @Query("select * from master where mid=:mid")
    fun selectMasterItem(mid: Int): LiveData<Master>

}