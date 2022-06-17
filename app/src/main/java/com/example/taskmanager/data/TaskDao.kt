package com.example.taskmanager.data

import androidx.room.*
import com.example.taskmanager.domain.Task
import java.util.*

@Dao
interface TaskDao {

    @Insert
    fun addTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("SELECT * FROM tasks t WHERE t.taskDate == :gregorianCalendar")
    fun getTasksByDay(gregorianCalendar: GregorianCalendar): List<Task>

    @Query("SELECT * FROM tasks t WHERE t.taskDate >= :start AND t.taskDate <= :end")
    fun getTasksInInterval(start: GregorianCalendar, end: GregorianCalendar): List<Task>
}