package com.example.taskmanager.domain.usecases

import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository

class AddTaskUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun addTask(task: Task){
        taskManagerRepository.addTask(task)
    }
}