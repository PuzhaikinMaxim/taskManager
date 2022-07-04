package com.example.taskmanager.domain.usecases

import androidx.lifecycle.LiveData
import com.example.taskmanager.domain.Statistics
import com.example.taskmanager.domain.TaskManagerRepository

class GetStatisticsUseCase(private val repository: TaskManagerRepository) {

    fun getStatistics() : LiveData<Statistics> {
        return repository.getStatistics()
    }
}