package com.example.project1.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MasterDao {
    @Insert
    fun insertMaster(master: Master)

    @Query("select * from master order by mid desc limit 1")
    fun selectLatestMaster(): LiveData<Master>

    @Query("select * from master where mid=:mid")
    fun selectMasterItem(mid: Int): LiveData<Master>

}