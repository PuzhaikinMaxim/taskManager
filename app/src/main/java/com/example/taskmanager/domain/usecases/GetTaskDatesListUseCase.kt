package com.example.taskmanager.domain.usecases

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Day
import com.example.taskmanager.domain.TaskManagerRepository
import java.util.*

class GetTaskDatesListUseCase(private val repository: TaskManagerRepository) {

    fun getTaskDatesList(start: GregorianCalendar, end: GregorianCalendar) : LiveData<List<Day>> {
        return repository.getTaskDatesList(start, end)
    }
}