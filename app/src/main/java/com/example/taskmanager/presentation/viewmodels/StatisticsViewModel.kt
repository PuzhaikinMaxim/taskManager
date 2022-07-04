package com.example.taskmanager.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.TaskManagerRepositoryImpl
import com.example.taskmanager.domain.usecases.GetStatisticsUseCase

class StatisticsViewModel : ViewModel() {

    private val repository = TaskManagerRepositoryImpl
    private val getStatisticsUseCase = GetStatisticsUseCase(repository)

    val statistics = getStatisticsUseCase.getStatistics()
}