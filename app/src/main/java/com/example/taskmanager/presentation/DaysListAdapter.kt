package com.example.taskmanager.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.domain.Day

class DaysListAdapter : RecyclerView.Adapter<DaysListAdapter.DaysListViewHolder>() {

    var onDayItemListener: ((Day)->Unit)? = null
    var daysList: List<Day> = listOf()
        set(value) {
            val callback = DayListDiffCallback(daysList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysListViewHolder {
        val itemView = LayoutInflater.
        from(parent.context).
        inflate(R.layout.item_day,parent,false)
        return DaysListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DaysListViewHolder, position: Int) {
        val day = daysList[position]
        holder.tvDay?.text = day.dayOfMonth.toString()
        if(day.isEmpty){
            holder.layoutDay?.setBackgroundColor(Color.LTGRAY)
        }
        if(day.isEveryTaskDone){
            holder.layoutDay?.setBackgroundColor(Color.GREEN)
        }
    }

    override fun getItemCount(): Int {
        return daysList.size
    }

    class DaysListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var layoutDay: LinearLayout? = null
        var tvDay: TextView? = null

        init {
            layoutDay = itemView.findViewById(R.id.l_day)
            tvDay = itemView.findViewById(R.id.tv_day)
        }
    }
}