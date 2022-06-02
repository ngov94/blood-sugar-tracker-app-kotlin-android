package com.example.project1.domain

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.project1.data.BloodSugar
import com.example.project1.data.BloodSugarRepository
import com.example.project1.data.DailyAvgStat
import kotlinx.coroutines.launch

class BloodSugarViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: BloodSugarRepository
    val allBloodSugar: LiveData<List<BloodSugar>>?
    val dailyAvgList: LiveData<List<DailyAvgStat>>?

    init {
        repo = BloodSugarRepository(app)
        allBloodSugar = repo.selectAllBloodSugar()
        dailyAvgList = repo.selectDailyAvg()
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