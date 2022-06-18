package com.example.taskmanager.domain.usecases

import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository

class EditTaskUseCase(private val taskManagerRepository: TaskManagerRepository) {

    fun editTask(task: Task){
        taskManagerRepository.editTask(task)
    }
}