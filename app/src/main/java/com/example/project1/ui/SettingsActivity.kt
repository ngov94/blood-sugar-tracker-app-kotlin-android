package com.example.project1.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project1.data.Master
import com.example.project1.domain.MasterViewModel
import com.example.project1.R
import com.example.project1.databinding.ActivitySettingsBinding
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

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

        // Field Validation
        hypoFocusListener()
        hyperFocusListener()
        tarLowFocusListener()
        tarHighFocusListener()

        mvm.latestMaster?.observe(this){ latestMaster ->
            getLatestMaster(latestMaster)
        }

        binding.clearForm.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Are you sure you want to clear the form?")
                .setPositiveButton("No"){ _,_ ->
                    // do nothing
                }
                .setNegativeButton("Yes") { _, _ ->
                    binding.hypoRange.setText("")
                    binding.hyperRange.setText("")
                    binding.tarLow.setText("")
                    binding.tarHigh.setText("")

                    binding.hypoRangeL.helperText = "Required"
                    binding.hyperRangeL.helperText = "Required"
                    binding.tarLowL.helperText = "Required"
                    binding.tarHighL.helperText = "Required"
                }.show()
        }

        setOnCheckedChangeListener()

        binding.applyChangesBtn.setOnClickListener {

            val invalidForm = AlertDialog.Builder(this)
                .setTitle("Invalid Settings")
                .setMessage("Missing or Invalid Values\n\nSettings Not Updated")
                .setPositiveButton("Okay"){ _,_ ->
                    // do nothing
                }

            if(hypoRangeL.helperText != null){
                hypoRangeL.helperText = null
                hypoRangeL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(hypoRangeL.error != null){
                invalidForm.show()
                return@setOnClickListener
            }
            else if(hyperRangeL.helperText != null){
                hyperRangeL.helperText = null
                hyperRangeL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(hyperRangeL.error != null){
                invalidForm.show()
                return@setOnClickListener
            }
            else if(tarLowL.helperText != null){
                tarLowL.helperText = null
                tarLowL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(tarLowL.error != null){
                invalidForm.show()
                return@setOnClickListener
            }
            else if(tarHighL.helperText != null){
                tarHighL.helperText = null
                tarHighL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(tarHighL.error != null){
                invalidForm.show()
                return@setOnClickListener
            }

            val ho = hypoRange.text.toString().toDouble()
            val hy = hyperRange.text.toString().toDouble()
            val tl = tarLow.text.toString().toDouble()
            val th = tarHigh.text.toString().toDouble()

            if(!(ho < tl && tl < th && th < hy)){
                AlertDialog.Builder(this)
                    .setTitle("Invalid Settings")
                    .setMessage("Range is Invalid\n\nSettings Not Updated")
                    .setPositiveButton("Okay"){ _,_ ->
                        // do nothing
                    }.show()
                return@setOnClickListener
            }

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

    private fun hypoFocusListener(){
        binding.hypoRange.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.hypoRangeL.helperText = null
                binding.hypoRangeL.error = validConc(binding.hypoRange.text.toString())
            }
        }
    }

    private fun hyperFocusListener(){
        binding.hyperRange.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.hyperRangeL.helperText = null
                binding.hyperRangeL.error = validConc(binding.hyperRange.text.toString())
            }
        }
    }

    private fun tarLowFocusListener(){
        binding.tarLow.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.tarLowL.helperText = null
                binding.tarLowL.error = validConc(binding.tarLow.text.toString())
            }
        }
    }

    private fun tarHighFocusListener(){
        binding.tarHigh.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.tarHighL.helperText = null
                binding.tarHighL.error = validConc(binding.tarHigh.text.toString())
            }
        }
    }

    private fun validConc(value: String): String?{

        val regex = Regex("\\d{1,3}(\\.\\d{0,1})?")
        if(value.equals("")){
            return "Required"
        }
        else if(!value.matches(regex)){
            return "Invalid Value"
        }

        return null
    }


}