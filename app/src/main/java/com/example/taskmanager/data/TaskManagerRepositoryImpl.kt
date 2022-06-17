package com.example.taskmanager.data

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository
import java.util.*

object TaskManagerRepositoryImpl : TaskManagerRepository {

    private val tasksList = sortedSetOf<Task>({
            o1,o2 ->
            o1.id.compareTo(o2.id)
            o1.taskDate.compareTo(o2.taskDate)
    })
    private val autoIncrementTaskId = 0

    override fun getTasksList(gregorianCalendar: GregorianCalendar): LiveData<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTask(): LiveData<Task> {
        TODO("Not yet implemented")
    }

    override fun addTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun removeTask(task: Task) {
        TODO("Not yet implemented")
    }

}