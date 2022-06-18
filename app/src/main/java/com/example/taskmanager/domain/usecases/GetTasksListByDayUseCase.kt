package com.example.taskmanager.domain.usecases

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository
import java.util.*

class GetTasksListByDayUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun getTasksListByDay(date: GregorianCalendar): LiveData<List<Task>> {
        return taskManagerRepository.getTasksList(date)
    }
}