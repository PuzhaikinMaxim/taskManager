package com.example.taskmanager.domain.usecases

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository
import java.util.*

class GetTasksListByIntervalUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun getTasksListByInterval(start: GregorianCalendar,
                               end: GregorianCalendar): LiveData<List<Task>>{
        return taskManagerRepository.getTasksList(start, end)
    }
}