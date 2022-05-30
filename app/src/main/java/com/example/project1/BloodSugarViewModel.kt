package com.example.project1

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BloodSugarViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: BloodSugarRepository
    val allBloodSugar: LiveData<List<BloodSugar>>?

    init {
        repo = BloodSugarRepository(app)
        allBloodSugar = repo.selectAllBloodSugar()
    }

    fun insertBloodSugar(bsugar: BloodSugar) = viewModelScope.launch{
        repo.insertBloodSugar(bsugar)
    }

    fun selectAllBloodSugar() = viewModelScope.launch{
        repo.selectAllBloodSugar()
    }

    fun selectBloodSugar(bsid: Int) = viewModelScope.launch{
        repo.selectBloodSugar(bsid)
    }

    fun selectDailyAvg() = viewModelScope.launch{
        repo.selectDailyAvg()
    }

    fun updateBloodSugar(bsugar: BloodSugar) = viewModelScope.launch{
        repo.updateBloodSugar(bsugar)
    }

    fun deleteBloodSugar(bsugar: BloodSugar) = viewModelScope.launch{
        repo.deleteBloodSugar(bsugar)
    }


}