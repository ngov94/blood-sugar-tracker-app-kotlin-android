package com.example.project1.domain

import android.app.Application
import androidx.lifecycle.*
import com.example.project1.data.BloodSugarMasterRepository
import kotlinx.coroutines.launch

class BloodSugarMasterViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: BloodSugarMasterRepository
//    val allBloodSugarMaster: LiveData<List<BSMaster>>?

    var searchString = MutableLiveData<String>()
    var totalResults: LiveData<Int>?

    var noSelectOffset = MutableLiveData<Int>()
    var NOOffset = MutableLiveData<Int>()
    var ONOffset = MutableLiveData<Int>()
    var HLOffset = MutableLiveData<Int>()
    var LHOffset = MutableLiveData<Int>()

    init {
        repo = BloodSugarMasterRepository(app)
//        allBloodSugarMaster = repo.selectAllBloodSugarMaster()

        totalResults = repo.selectTotalBloodSugarMaster()

        searchString.value = ""
        noSelectOffset.value = 0
        NOOffset.value = 0
        ONOffset.value = 0
        HLOffset.value = 0
        LHOffset.value = 0
    }

    // Search By Notes
    val searchStringBSM = Transformations.switchMap(searchString){ search ->
        if(search != ""){
            repo.selectAllBSMNotes(search)
        }
        else{
            repo.selectAllBloodSugarMasterPage(0)
        }
    }

    fun selectAllBSMNotes(searchnotes: String) = viewModelScope.launch{
        searchString.value = searchnotes
    }


    // Pagination
    val bsmPages = Transformations.switchMap(noSelectOffset){ offset ->
        repo.selectAllBloodSugarMasterPage(offset)
    }
    fun selectAllBloodSugarMasterPage(offset: Int) = viewModelScope.launch{
        noSelectOffset.value = offset
    }

    //Sorting - Pagination
    // New to Old
    val allBSMNewtoOld = Transformations.switchMap(NOOffset){ offset ->
        repo.selectAllBloodSugarMasterNewtoOld(offset)
    }
    fun selectAllBloodSugarMasterNewtoOld(offset: Int) = viewModelScope.launch{
        NOOffset.value = offset
    }

    // Old to New
    val allBSMOldtoNew = Transformations.switchMap(ONOffset){ offset ->
        repo.selectAllBloodSugarMasterOldtoNew(offset)
    }
    fun selectAllBloodSugarMasterOldtoNew(offset: Int) = viewModelScope.launch{
        ONOffset.value = offset
    }

    // High to Low
    val allBSMHightoLow = Transformations.switchMap(HLOffset){ offset ->
        repo.selectAllBloodSugarMasterHightoLow(offset)
    }
    fun selectAllBloodSugarMasterHightoLow(offset: Int) = viewModelScope.launch{
        HLOffset.value = offset
    }

    // Low to High
    val allBSMLowtoHigh = Transformations.switchMap(LHOffset){ offset ->
        repo.selectAllBloodSugarMasterLowtoHigh(offset)
    }
    fun selectAllBloodSugarMasterLowtoHigh(offset: Int) = viewModelScope.launch{
        LHOffset.value = offset
    }
}