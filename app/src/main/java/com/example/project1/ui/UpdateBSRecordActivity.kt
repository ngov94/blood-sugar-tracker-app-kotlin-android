package com.example.project1.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.project1.data.BloodSugar
import com.example.project1.domain.BloodSugarViewModel
import com.example.project1.data.Master
import com.example.project1.R
import com.example.project1.databinding.ActivityUpdateBsrecordBinding
import kotlinx.android.synthetic.main.activity_add_bsrecord.*
import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class UpdateBSRecordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBsrecordBinding
    lateinit var bvm: BloodSugarViewModel
    lateinit var latestMas: Master

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBsrecordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar!!.title = "Update Blood Sugar Record"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bvm = BloodSugarViewModel(application)

        //Field validation
        sugarConcFocusListener()

        val intent = getIntent()
        val bsid = intent.getIntExtra("bsid", -1)
        val mid = intent.getIntExtra("mid", -1)


        binding.sugarConc.setText(intent.getStringExtra("sugarConc"))
        binding.autoCompleteMeasured.setText(intent.getStringExtra("measured"))
        binding.date.setText(intent.getStringExtra("date"))
        binding.time.setText(intent.getStringExtra("time"))
        binding.notes.setText(intent.getStringExtra("notes"))
        binding.unit.setText(intent.getStringExtra("unit"))

        binding.clearForm.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Are you sure you want to clear the form?")
                .setPositiveButton("No"){ _,_ ->
                    // do nothing
                }
                .setNegativeButton("Yes"){ _,_ ->
                    binding.sugarConc.setText("")
                    binding.autoCompleteMeasured.setText("")
                    binding.date.setText("")
                    binding.time.setText("")
                    binding.notes.setText("")

                    binding.sugarConcL.helperText = "Required"
                    binding.measuredmenu.helperText = "Required"
                    binding.dateL.helperText = "Required"
                    binding.timeL.helperText = "Required"
                }.show()
        }

        val measuredList = resources.getStringArray(R.array.measured)
        val arrayAdapter = ArrayAdapter(this, R.layout.menu_item_layout, measuredList)
        binding.autoCompleteMeasured.setAdapter(arrayAdapter)

        binding.autoCompleteMeasured.setOnItemClickListener { adapterView, view, i, l ->
            binding.measuredmenu.helperText = null
            binding.measuredmenu.error = null
        }

        binding.date.transformIntoDatePicker(this,"MMM d, yyyy", Date.valueOf(LocalDate.now().toString()))
        //Take whatever is in edit text and transform into date

        binding.time.transformIntoTimePicker(this, "HH:mm")

        binding.btnUpdateRecord.setOnClickListener {

            val invalidForm = AlertDialog.Builder(this)
                .setTitle("Invalid Form")
                .setMessage("Missing or Invalid Values\nRecord Not Updated")
                .setPositiveButton("Okay"){ _,_ ->
                    // do nothing
                }

                //Toast.makeText(this, "Invalid Record! Record Not Updated", Toast.LENGTH_LONG)

            if(sugarConcL.helperText != null){
                sugarConcL.helperText = null
                sugarConcL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(sugarConcL.error != null){
                invalidForm.show()
                return@setOnClickListener
            }
            else if(measuredmenu.helperText != null || measuredmenu.error != null){
                measuredmenu.helperText = null
                measuredmenu.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(dateL.helperText != null || dateL.error != null){
                dateL.helperText = null
                dateL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }
            else if(timeL.helperText != null || timeL.error != null){
                timeL.helperText = null
                timeL.error = "Required"
                invalidForm.show()
                return@setOnClickListener
            }


            binding.btnUpdateRecord.isEnabled = false

            var sc = binding.sugarConc.text.toString().toDouble()
            var mes = binding.autoCompleteMeasured.text.toString()
            var da = binding.date.text.toString()
            var dt = Date(SimpleDateFormat("MMM d, yyyy", Locale.CANADA).parse(da).time)
            var ti = binding.time.text.toString()
            var tm = Time(SimpleDateFormat("HH:mm").parse(ti).time)
            var not = binding.notes.text.toString()

            //update to bloodsugar database if mid is not null
           mid?.let { it1 ->  bvm.updateBloodSugar(BloodSugar(bsid = bsid, sugarConc = sc, measured = mes, date = dt, time = tm, notes = not, mid = it1)) } ///////////////

            Toast.makeText(this, "Record Updated!", Toast.LENGTH_LONG).show()
            var bloodSugarIntent = Intent(this, BloodSugarRecordsActivity::class.java)
            startActivity(bloodSugarIntent)

        }

    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.dateL.helperText = null
                binding.dateL.error = null
                val sdf = SimpleDateFormat(format, Locale.CANADA)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

    private fun EditText.transformIntoTimePicker(context: Context, format: String) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            binding.timeL.helperText = null
            binding.timeL.error = null
            binding.time.setText(SimpleDateFormat(format).format(cal.time))
        }

        setOnClickListener {
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

    }

    private fun sugarConcFocusListener(){
        binding.sugarConc.setOnFocusChangeListener { _, focused ->
            if(!focused){
                binding.sugarConcL.helperText = null
                binding.sugarConcL.error = validConc()
            }
        }
    }

    private fun validConc(): String?{
        val sugarConcText = binding.sugarConc.text.toString()
        val regex = Regex("\\d{1,3}(\\.\\d{0,1})?")
        if(sugarConcText.equals("")){
            return "Required"
        }
        else if(!sugarConcText.matches(regex)){
            return "Invalid Value"
        }

        return null
    }
}