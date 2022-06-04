package com.example.project1.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.*
import com.example.project1.data.BSMaster
import com.example.project1.data.BloodSugar
import com.example.project1.databinding.ActivityBloodSugarRecordsBinding
import com.example.project1.domain.BloodSugarMasterViewModel
import com.example.project1.domain.BloodSugarViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_blood_sugar_records.*
import kotlinx.android.synthetic.main.bs_list_item_layout.view.*
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class BloodSugarRecordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBloodSugarRecordsBinding
    lateinit var bsmvm: BloodSugarMasterViewModel
    lateinit var bsvm: BloodSugarViewModel
    var bsmList = ArrayList<BSMaster>()

    var bsmListCurrent = ArrayList<BSMaster>()

    var bsmListNewtoOld = ArrayList<BSMaster>()
    var bsmListOldtoNew = ArrayList<BSMaster>()
    var bsmListHightoLow = ArrayList<BSMaster>()
    var bsmListLowtoHigh = ArrayList<BSMaster>()

    var p = 0
    var totalItems = 0
    var pmax = 0
    val LIMIT = 5
    var selection = ""

    lateinit var progressLayout : FrameLayout

    var adapter: BloodSugarAdapter =  BloodSugarAdapter(bsmList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodSugarRecordsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        progressLayout = findViewById(R.id.progress_overlay)

        supportActionBar!!.title = "Blood Sugar Records"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bsmvm = BloodSugarMasterViewModel(application)
        bsvm = BloodSugarViewModel(application)


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)


        // Pagnation
        bsmvm.bsmPages?.observe(this){  bsmList ->
            this.bsmListCurrent.clear()
            this.bsmListCurrent.addAll(bsmList)
            getBloodSugarRecords(bsmList)
            nextPrevButtonVisability()
            progressGone()

        }

        bsmvm.totalResults?.observe(this){
            totalItems = it
            pmax = (totalItems-1)/LIMIT
            nextPrevButtonVisability()
        }

        binding.nextPage.setOnClickListener {
            progressVisable()
            var offset = ++p * LIMIT
            pageloader(offset)
        }

        binding.prevPage.setOnClickListener {
            progressVisable()
            var offset = --p * LIMIT
            pageloader(offset)
        }

        //Sorting
        bsmvm.allBSMNewtoOld?.observe(this){ sortbsmList ->
            bsmListNewtoOld.clear()
            bsmListNewtoOld.addAll(sortbsmList)
            if(selection.equals("New to Old")){
                getBloodSugarRecords(bsmListNewtoOld)
                nextPrevButtonVisability()
                progressGone()

            }
        }

        bsmvm.allBSMOldtoNew?.observe(this){ sortbsmList ->
            bsmListOldtoNew.clear()
            bsmListOldtoNew.addAll(sortbsmList)
            if(selection.equals("Old to New")){
                getBloodSugarRecords(bsmListOldtoNew)
                nextPrevButtonVisability()
                progressGone()
            }
        }

        bsmvm.allBSMHightoLow?.observe(this){ sortbsmList ->
            bsmListHightoLow.clear()
            bsmListHightoLow.addAll(sortbsmList)
            if(selection.equals("High to Low")){
                getBloodSugarRecords(bsmListHightoLow)
                nextPrevButtonVisability()
                progressGone()

            }
        }

        bsmvm.allBSMLowtoHigh?.observe(this){ sortbsmList ->
            bsmListLowtoHigh.clear()
            bsmListLowtoHigh.addAll(sortbsmList)
            if(selection.equals("Low to High")){
                getBloodSugarRecords(bsmListLowtoHigh)
                nextPrevButtonVisability()

                progressGone()

            }
        }


        val items = listOf("Old to New", "New to Old", "High to Low", "Low to High")
        val adapterArray = ArrayAdapter(this, R.layout.menu_item_layout, items)
        binding.autoCompleteSortBy.setAdapter(adapterArray)

        binding.autoCompleteSortBy.setOnItemClickListener { adapterView, view, i, l ->
            var selectedSort = adapterView.getItemAtPosition(i) as String
            this.p = 0
            progressVisable()

            nextPrevButtonVisability()
            when(selectedSort){
                "Old to New" -> {getBloodSugarRecords(this.bsmListOldtoNew)
                                selection = "Old to New"}
                "New to Old" -> {getBloodSugarRecords(this.bsmListNewtoOld)
                                selection = "New to Old"}
                "High to Low" -> {getBloodSugarRecords(this.bsmListHightoLow)
                                selection = "High to Low"}
                "Low to High" -> {getBloodSugarRecords(this.bsmListLowtoHigh)
                                selection = "Low to High"}
            }

            progressGone()
            Toast.makeText(this, "${selectedSort} has been selected", Toast.LENGTH_LONG).show()
        }

        // Search
        bsmvm.searchStringBSM?.observe(this){ searchbsmList ->
            getBloodSugarRecords(searchbsmList)
        }

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchItem: String?): Boolean {
                var searchItemTrim = searchItem.toString().trim()
                if((searchItemTrim != null) && (!searchItemTrim.equals(""))){
                    var upperSearchItem = "%"+searchItemTrim.toString().uppercase(Locale.getDefault())+"%"
                    bsmvm.selectAllBSMNotes(upperSearchItem)
                }
                return false
            }
            override fun onQueryTextChange(searchItem: String?): Boolean {
                return false
            }
        })

        binding.searchView.setOnCloseListener {
            getBloodSugarRecords(this.bsmListCurrent)
            false
        }


        // Insert Blood Record
        binding.btnAddBSRecord.setOnClickListener {
            val addBSRecordActivityIntent = Intent(this, AddBSRecordActivity::class.java)
            startActivity(addBSRecordActivityIntent)
        }

        // Update Blood Record
        adapter.setOnItemClickListener(object : BloodSugarAdapter.OnItemClickListener {
            override fun onItemClick(bsmView: View) {
                println("Clicked "+ bsmView.bsid_card.text.toString())
                var bsid = bsmView.bsid_card.text.toString().toInt()
                var sugarConc = bsmView.sugar_conc_card.text.toString()
                var measured = bsmView.measured_card.text.toString()
                var date = bsmView.date_card.text.toString()
                var time = bsmView.time_card.text.toString()
                var notes = bsmView.notes_card.text.toString()
                var mid = bsmView.mid_card.text.toString().toInt()
                var unit = bsmView.sugar_unit_card.text.toString()



                val intentUpdateBS = Intent(this@BloodSugarRecordsActivity, UpdateBSRecordActivity::class.java)
                intentUpdateBS.putExtra("bsid",bsid)
                intentUpdateBS.putExtra("sugarConc", sugarConc)
                intentUpdateBS.putExtra("measured", measured)
                intentUpdateBS.putExtra("date", date)
                intentUpdateBS.putExtra("time", time)
                intentUpdateBS.putExtra("notes", notes)
                intentUpdateBS.putExtra("mid", mid)
                intentUpdateBS.putExtra("unit", unit)
                startActivity(intentUpdateBS)

            }
        })

        // Delete Blood Record
        adapter.setOnItemLongClickListener(object : BloodSugarAdapter.OnItemLongClickListener {
            override fun onItemLongClick(bsmView: View) {
                println("Long Click "+ bsmView.bsid_card.text.toString())

                var bsid = bsmView.bsid_card.text.toString().toInt()
                var sugarConc = bsmView.sugar_conc_card.text.toString().toDouble()
                var measured = bsmView.measured_card.text.toString()
                var da = bsmView.date_card.text.toString()
                var date: Date = Date(SimpleDateFormat("MMM d, yyyy", Locale.CANADA).parse(da).time)
                var ti = bsmView.time_card.text.toString()
                var time: Time = Time(SimpleDateFormat("HH:mm", Locale.CANADA).parse(ti).time)
                var notes = bsmView.notes_card.text.toString()
                var mid = bsmView.mid_card.text.toString().toInt()

                var deleteRecord = BloodSugar(bsid,sugarConc,measured,date,time,notes,mid)



                val snackbarYes = Snackbar.make(bloodsugarrecordsView!!, "Record Deleted", Snackbar.LENGTH_LONG)

                val snackbarNo = Snackbar.make(bloodsugarrecordsView!!, "Record Restored", Snackbar.LENGTH_LONG)


                AlertDialog.Builder(this@BloodSugarRecordsActivity)
                    .setTitle("Confirm Deletion")
                    .setNegativeButton("Yes"){ _,_ ->
                        bsvm.deleteBloodSugar(deleteRecord)
                        snackbarYes.show()
                    }
                    .setPositiveButton("No"){ _,_ ->
                        binding.autoCompleteSortBy.setText("")
                        snackbarNo.show()
                    }.show()
            }
        })

    }

    fun getBloodSugarRecords(bSMList: List<BSMaster>){
        this.bsmList.clear()
        this.bsmList.addAll(bSMList)
        if(this.bsmList.isEmpty()){
            binding.recyclerView.visibility = View.INVISIBLE
            binding.noRecords.visibility = View.VISIBLE
        }
        else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.noRecords.visibility = View.GONE
        }

        adapter.notifyDataSetChanged()//let adapter know the data changedkvjk
    }

    fun nextPrevButtonVisability(){
        if (pmax>p){
            binding.nextPage.visibility = View.VISIBLE
        }
        else{
            binding.nextPage.visibility = View.INVISIBLE
        }

        if(p != 0){
            binding.prevPage.visibility = View.VISIBLE
        }
        else{
            binding.prevPage.visibility = View.INVISIBLE
        }
    }

    fun pageloader(offset: Int){
        when(selection){
            "Old to New" -> bsmvm.selectAllBloodSugarMasterOldtoNew(offset)
            "New to Old" -> bsmvm.selectAllBloodSugarMasterNewtoOld(offset)
            "High to Low" -> bsmvm.selectAllBloodSugarMasterHightoLow(offset)
            "Low to High" -> bsmvm.selectAllBloodSugarMasterLowtoHigh(offset)
            else -> bsmvm.selectAllBloodSugarMasterPage(offset)
        }
    }

    fun progressVisable(){
        progressLayout.visibility = View.VISIBLE
    }

    fun progressGone(){
        Handler().postDelayed({
            progressLayout.visibility = View.GONE
        }, 500)
    }


}