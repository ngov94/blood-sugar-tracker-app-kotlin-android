package com.example.project1.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.project1.data.Master
import com.example.project1.domain.MasterViewModel
import com.example.project1.R
import com.example.project1.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var mvm: MasterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar!!.title = "Blood Sugar Tracker"

        mvm = MasterViewModel(application)

        mvm.latestMaster?.observe(this){ latestMaster ->
            isMasterNull(latestMaster)
        }

        binding.bsrecords.setOnClickListener {
            var bsRecordsIntent = Intent(this, BloodSugarRecordsActivity::class.java)
            startActivity(bsRecordsIntent)
        }

        binding.stats.setOnClickListener {
            var statIntent = Intent(this, AnalyticsActivity::class.java)
            startActivity(statIntent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var settingsActivityIntent = Intent(this, SettingsActivity::class.java)
        startActivity(settingsActivityIntent)

        return super.onOptionsItemSelected(item)
    }

    private fun  isMasterNull(latest: Master?) {
        if (latest == null) {
            var intialMaster = Master(
                sugarUnit = "mmol/L",
                hypo = 3.9,
                hyper = 10.0,
                tarRangeLow = 5.0,
                tarRangeHigh = 8.9
            )
            mvm.insertMaster(intialMaster)
        }
    }
}

