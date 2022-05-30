package com.example.project1

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1.databinding.ActivityBloodSugarRecordsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_bsrecord.view.*
import kotlinx.android.synthetic.main.activity_blood_sugar_records.*
import kotlinx.android.synthetic.main.bs_list_item_layout.view.*
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat


class BloodSugarRecords : AppCompatActivity() {
    private lateinit var binding: ActivityBloodSugarRecordsBinding
    lateinit var bsmvm: BloodSugarMasterViewModel
    lateinit var bsvm: BloodSugarViewModel
    var bsmList = ArrayList<BSMaster>()
    var adapter: BloodSugarAdapter =  BloodSugarAdapter(bsmList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBloodSugarRecordsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar!!.title = "Blood Sugar Records"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bsmvm = BloodSugarMasterViewModel(application)
        bsvm = BloodSugarViewModel(application)


        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        bsmvm.allBloodSugarMaster?.observe(this){ bsmList ->
            getBloodSugarRecords(bsmList)
        }

        //Sorting
        val items = listOf("Old to New", "New to Old", "High to Low", "Low to High")
        val adapterArray = ArrayAdapter(this, R.layout.menu_item_layout, items)
        binding.autoCompleteSortBy.setAdapter(adapterArray)

        binding.autoCompleteSortBy.setOnItemClickListener { adapterView, view, i, l ->
            var selectedSort = adapterView.getItemAtPosition(i) as String
            Toast.makeText(this, "${selectedSort} has been selected", Toast.LENGTH_LONG).show()
        }

        // Insert Blood Record
        binding.btnAddBSRecord.setOnClickListener {
            val addBSRecordIntent = Intent(this, AddBSRecord::class.java)
            startActivity(addBSRecordIntent)
        }

        // Update Blood Record
        adapter.setOnItemClickListener(object : BloodSugarAdapter.OnItemClickListener{
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



                val intentUpdateBS = Intent(this@BloodSugarRecords,UpdateBSRecord::class.java)
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
        adapter.setOnItemLongClickListener(object : BloodSugarAdapter.OnItemLongClickListener{
            override fun onItemLongClick(bsmView: View) {
                println("Long Click "+ bsmView.bsid_card.text.toString())

                var bsid = bsmView.bsid_card.text.toString().toInt()
                var sugarConc = bsmView.sugar_conc_card.text.toString().toDouble()
                var measured = bsmView.measured_card.text.toString()
                var da = bsmView.date_card.text.toString()
                var date: Date = Date(SimpleDateFormat("MMM d, yyyy").parse(da).time)
                var ti = bsmView.time_card.text.toString()
                var time: Time = Time(SimpleDateFormat("HH:mm").parse(ti).time)
                var notes = bsmView.notes_card.text.toString()
                var mid = bsmView.mid_card.text.toString().toInt()

                var deleteRecord = BloodSugar(bsid,sugarConc,measured,date,time,notes,mid)

                bsvm.deleteBloodSugar(deleteRecord)

                val snackbarYes = Snackbar.make(bloodsugarrecordsView!!, "Record Deleted", Snackbar.LENGTH_LONG)

                val snackbarNo = Snackbar.make(bloodsugarrecordsView!!, "Record Restored", Snackbar.LENGTH_LONG)


                AlertDialog.Builder(this@BloodSugarRecords)
                    .setTitle("Confirm Deletion")
                    .setPositiveButton("Yes"){ _,_ ->
                        snackbarYes.show()
                    }
                    .setNegativeButton("No"){ _,_ ->
                        bsvm.insertBloodSugar(deleteRecord)
                        snackbarNo.show()
                    }

//                val snackbar = Snackbar.make(bloodsugarrecordsView!!, "Record Deleted", Snackbar.LENGTH_LONG)
//                    .setAction("UNDO", object : View.OnClickListener {
//                        override fun onClick(view: View) {
//                            val snackbar1 = Snackbar.make(
//                                bloodsugarrecordsView!!,
//                                "Record Restored",
//                                Snackbar.LENGTH_SHORT
//                            )
//                            bsvm.insertBloodSugar(deleteRecord)
//                            snackbar1.show()
//                            return
//                        }
//                    })
//                snackbar.show()

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


        adapter.notifyDataSetChanged()//let adapter know the data changed
    }

}