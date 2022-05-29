package com.example.project1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MasterViewModel(app: Application): AndroidViewModel(app) {

    private val repo: MasterRepository
    val latestMaster: LiveData<Master>?

    init {
        repo = MasterRepository(app)
        latestMaster = repo.selectLatestMaster()
    }

    fun insertMaster(master: Master) = viewModelScope.launch{
        repo.insertMaster(master)
    }

    fun selectLatestMaster() = viewModelScope.launch{
        repo.selectLatestMaster()
    }

    fun selectMasterItem(mid: Int) = viewModelScope.launch{
        repo.selectMasterItem(mid)
    }
}