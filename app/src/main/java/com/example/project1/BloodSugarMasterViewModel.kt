package com.example.project1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class BloodSugarMasterViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: BloodSugarMasterRepository
    val allBloodSugarMaster: LiveData<List<BSMaster>>?

    init {
        repo = BloodSugarMasterRepository(app)
        allBloodSugarMaster = repo.selectAllBloodSugarMaster()
    }
}