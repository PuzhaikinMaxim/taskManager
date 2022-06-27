package com.example.taskmanager.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.domain.PriorityTypes
import com.example.taskmanager.domain.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    var onTaskItemListener: ((Task)-> Unit)? = null

    var taskList: List<Task> = listOf()
        set(value) {
            val callback = TaskListDiffCallback(taskList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.
        from(parent.context).
        inflate(R.layout.item_task,parent,false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = taskList[position]
        holder.taskText?.text = item.taskName
        holder.taskDescription?.text = item.taskDescription
        val backgroundColor = when(item.priorityType) {
            PriorityTypes.HIGH_PRIORITY -> Color.RED
            PriorityTypes.MEDIUM_PRIORITY -> Color.YELLOW
            PriorityTypes.LOW_PRIORITY -> Color.GREEN
        }
        holder.taskCardBackground?.setBackgroundColor(backgroundColor)
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val date = taskList[position].taskDate.time
        holder.taskDate?.text = dateFormatter.format(date)
        holder.itemView.setOnClickListener {
            onTaskItemListener?.invoke(item)
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskText: TextView? = null
        var taskDescription: TextView? = null
        var taskDate: TextView? = null
        var taskCardBackground: ConstraintLayout? = null

        init {
            taskText = itemView.findViewById(R.id.taskText)
            taskDescription = itemView.findViewById(R.id.taskDescription)
            taskDate = itemView.findViewById(R.id.taskDate)
            taskCardBackground = itemView.findViewById(R.id.taskCardBackground)
        }
    }
}