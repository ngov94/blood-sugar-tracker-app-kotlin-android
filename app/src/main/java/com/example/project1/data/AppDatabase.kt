package com.example.project1.data

import android.content.Context
import androidx.room.*

@Database(entities = [Master::class, BloodSugar::class], version = 1, exportSchema = false)
@TypeConverters(Convertor::class)
//3 - abstract and extend
abstract class AppDatabase : RoomDatabase() {
    //4 - abstract and return Dao
    abstract fun masterDao() : MasterDao
    abstract fun bloodSugarDao() : BloodSugarDao
    abstract fun bsMasterDao() : BloodSugarMasterDao

    //5 - Singleton because you don't want multiple connections to the database
    //This will always be the same, regardless of the program
    companion object{
        var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context) : AppDatabase?{
            if(INSTANCE == null){
                //6 - we are acquiring an instance of RoomDB builder
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "bloodsugarapp.db")
                        .allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}