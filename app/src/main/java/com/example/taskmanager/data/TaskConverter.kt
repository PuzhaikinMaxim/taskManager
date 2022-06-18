package com.example.taskmanager.data

import com.example.taskmanager.domain.Task

class TaskConverter {

    companion object {
        fun convertToTable(task: Task): TaskTable{
            return with(task){
                TaskTable(taskName,
                    taskDescription,
                    taskDate,
                    priorityType,
                    isOutdated,
                    isDone,
                    id
                )
            }
        }

        fun convertFromTable(taskTable: TaskTable): Task{
            return with(taskTable){
                Task(
                    taskName,
                    taskDescription,
                    taskDate,
                    priorityType,
                    isOutdated,
                    isDone,
                    id
                )
            }
        }
    }
}