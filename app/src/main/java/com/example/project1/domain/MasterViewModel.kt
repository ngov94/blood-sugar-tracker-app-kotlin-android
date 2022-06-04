package com.example.project1.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.project1.data.Master
import com.example.project1.data.MasterRepository
import kotlinx.coroutines.launch

class MasterViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: MasterRepository
    val latestMaster: LiveData<Master>?

    init {
        repo = MasterRepository(app)
        latestMaster = repo.selectLatestMaster()
    }

    fun insertMaster(master: Master) = viewModelScope.launch{
        repo.insertMaster(master)
    }

}