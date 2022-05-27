package com.example.project1

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BloodSugarDao {

    @Insert
    fun insertBloodSugar(bsugar: BloodSugar)

    @Query("select * from bloodsugar")
    fun selectAllBloodSugar(): LiveData<List<BloodSugar>>

    @Update
    fun updateBloodSugar(bsugar: BloodSugar)

    @Delete
    fun deleteBloodSugar(bsugar: BloodSugar)

}