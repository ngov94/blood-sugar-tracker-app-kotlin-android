package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project1.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*


class Settings : AppCompatActivity() {

    lateinit var mvm: MasterViewModel
    private lateinit var binding: ActivitySettingsBinding
    lateinit var latestMaster: Master
    val mmol = "mmol/L"
    val mg = "mg/dL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar!!.title = "Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mvm = MasterViewModel(application)

        mvm.latestMaster?.observe(this){ latestMaster ->
            getLatestMaster(latestMaster)
        }

        setOnCheckedChangeListener()

        binding.applyChangesBtn.setOnClickListener {
            binding.applyChangesBtn.isEnabled = false

            var sugarUnit: String
            var sugarUnitID = binding.radioSugarUnit.checkedRadioButtonId

            sugarUnit = if (sugarUnitID.equals(binding.radiommolL.id)) mmol else mg

            var hypo =  binding.hypoRange.text.toString().toDouble()
            var tarLow = binding.tarLow.text.toString().toDouble()
            var tarHigh = binding.tarHigh.text.toString().toDouble()
            var hyper = binding.hyperRange.text.toString().toDouble()

            var insertMast = Master(sugarUnit = sugarUnit, hypo = hypo, hyper = hyper, tarRangeLow = tarLow, tarRangeHigh = tarHigh)

            mvm.insertMaster(insertMast)

            Toast.makeText(this, "Changes Applied", Toast.LENGTH_LONG).show()
            var mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun setOnCheckedChangeListener() {
        binding.radioSugarUnit.setOnCheckedChangeListener { group, checkedId ->
            val sugarUnit = if (R.id.radiommolL == checkedId) mmol else mg
            binding.unit1.text = sugarUnit
            binding.unit2.text = sugarUnit
            binding.unit3.text = sugarUnit
            binding.unit4.text = sugarUnit
        }
    }

    private fun getLatestMaster(latest: Master?){
        if (latest != null) {
            this.latestMaster = latest
        }
        else{
            var intialMaster = Master(sugarUnit = mmol, hypo = 3.9, hyper = 10.0, tarRangeLow = 5.0, tarRangeHigh = 8.9)
            mvm.insertMaster(intialMaster)
            return
        }

        binding.radioSugarUnit.clearCheck()

        if(latestMaster.sugarUnit.equals(mmol)){
            binding.radioSugarUnit.check(radiommolL.id)
        }
        else{
            binding.radioSugarUnit.check(radiomgdL.id)
        }

        binding.hypoRange.setText(latestMaster.hypo.toString())
        binding.hyperRange.setText(latestMaster.hyper.toString())
        binding.tarLow.setText(latestMaster.tarRangeLow.toString())
        binding.tarHigh.setText(latestMaster.tarRangeHigh.toString())

    }


}