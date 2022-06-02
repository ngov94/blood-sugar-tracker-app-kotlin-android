package com.example.project1

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.data.BSMaster
import java.text.SimpleDateFormat
import java.util.*

class BloodSugarAdapter(private val bsrList: List<BSMaster>) : RecyclerView.Adapter<ViewHolder>(){

    private lateinit var itemListener: OnItemClickListener

    private lateinit var itemLongListener: OnItemLongClickListener

    interface OnItemClickListener{
        fun onItemClick (itemView: View)
    }

    interface OnItemLongClickListener{
        fun onItemLongClick (itemView: View)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        itemListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener){
        itemLongListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bloodSugarItemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.bs_list_item_layout, parent, false)
        return ViewHolder(bloodSugarItemView, itemListener, itemLongListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = bsrList[position]

//        val dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
//        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        var sugCon = item.sugarConc
        var tarLow = item.tarRangeLow
        var tarHigh = item.tarRangeHigh
        var hypo = item.hypo
        var hyper = item.hyper

        holder.sugarConcTextView.text = sugCon.toString()

        if (sugCon in tarLow..tarHigh){
            holder.sugarConcTextView.setTextColor(Color.parseColor("#46A14A")) // GREEN
        } else if ((sugCon > hypo && sugCon < tarLow) || (sugCon < hyper && sugCon > tarHigh)) {
            holder.sugarConcTextView.setTextColor(Color.parseColor("#F69609")) // ORANGE
        } else{
            holder.sugarConcTextView.setTextColor(Color.parseColor("#CC2518")) // RED
        }

        holder.sugarUnitTextView.text = item.sugarUnit
        holder.dateTextView.setText(SimpleDateFormat("MMM d, yyyy", Locale.CANADA).format(item.date.time))
        holder.timeTextView.setText(SimpleDateFormat("HH:mm").format(item.time.time))
        holder.measuredTextView.text = item.measured
        holder.notesTextView.text = item.notes
        holder.bsidTextView.text = item.bsid.toString()
        holder.midTextView.text = item.mid.toString()

    }

    override fun getItemCount(): Int {
        return bsrList.size
    }


}

class ViewHolder(view: View, listener: BloodSugarAdapter.OnItemClickListener, longListener: BloodSugarAdapter.OnItemLongClickListener) : RecyclerView.ViewHolder(view){

    var sugarConcTextView: TextView = view.findViewById(R.id.sugar_conc_card)
    var sugarUnitTextView: TextView = view.findViewById(R.id.sugar_unit_card)
    var dateTextView: TextView = view.findViewById(R.id.date_card)
    var timeTextView: TextView = view.findViewById(R.id.time_card)
    var measuredTextView: TextView = view.findViewById(R.id.measured_card)
    var notesTextView: TextView = view.findViewById(R.id.notes_card)
    var bsidTextView: TextView = view.findViewById(R.id.bsid_card)
    var midTextView: TextView = view.findViewById(R.id.mid_card)

    init {
        view.setOnClickListener {
            listener.onItemClick(itemView)
        }
        view.setOnLongClickListener {
            longListener.onItemLongClick((itemView))
            return@setOnLongClickListener true
        }
    }



}