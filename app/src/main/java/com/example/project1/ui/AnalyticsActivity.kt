package com.example.project1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.project1.domain.BloodSugarViewModel
import com.example.project1.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class AnalyticsActivity : AppCompatActivity() {
    lateinit var bsvm: BloodSugarViewModel
    var lineListDA = ArrayList<Entry>()
    var xvalueDA = ArrayList<String>()

    var lineListAR = ArrayList<Entry>()
    var xvalueAR = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics)

        supportActionBar!!.title = "Blood Sugar Analytics"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        bsvm = BloodSugarViewModel(application)
        var lineChart = findViewById<LineChart>(R.id.lineChart)
        var dailyAvgBtn = findViewById<Button>(R.id.chartDailyAvgBTN)
        var allRecBtn = findViewById<Button>(R.id.chartAllRecordsBTN)

        var j = 0
        bsvm.dailyAvgList?.observe(this){
            for(i in it){
                xvalueDA.add(i.day.toString())
                lineListDA.add(Entry(i.dailyAvg.toFloat(), j++))
            }
        }

        var k = 0
        var prev = ""
        bsvm.allBloodSugar?.observe(this){
            for(i in it){
                xvalueAR.add(i.date.toString())
                if(i.date.toString().equals(prev)){
                    k--
                }
                lineListAR.add(Entry(i.sugarConc.toFloat(), k++))
                prev = i.date.toString()
            }
        }


        dailyAvgBtn.setOnClickListener {
            var lineDataSet = LineDataSet(lineListDA, "Daily Average Blood Sugar")
            lineDataSet.color = resources.getColor(R.color.purple_700)
            lineDataSet.valueTextSize = 18f

            var lineData = LineData(xvalueDA,lineDataSet)

            lineChart.data = lineData
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis.textSize = 15f
            lineChart.axisRight.textSize = 15f
            lineChart.axisLeft.textSize = 15f
            lineChart.setBackgroundColor(resources.getColor(R.color.white))
            lineChart.animateXY(3000, 3000)
        }

        allRecBtn.setOnClickListener {
            var lineDataSet = LineDataSet(lineListAR, "All Blood Sugar Records")
            lineDataSet.color = resources.getColor(R.color.teal_700)
            lineDataSet.valueTextSize = 18f

            var lineData = LineData(xvalueAR,lineDataSet)

            lineChart.data = lineData
            lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            lineChart.xAxis.textSize = 15f
            lineChart.axisRight.textSize = 15f
            lineChart.axisLeft.textSize = 15f
            lineChart.setBackgroundColor(resources.getColor(R.color.white))
            lineChart.animateXY(3000, 3000)
        }




    }


}