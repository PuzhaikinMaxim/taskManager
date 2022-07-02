package com.example.taskmanager.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.taskmanager.domain.Day

class DayListDiffCallback(private val oldList: List<Day>,
                          private val newList: List<Day>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].dayOfMonth == oldList[oldItemPosition].dayOfMonth
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition] == oldList[oldItemPosition]
    }
}