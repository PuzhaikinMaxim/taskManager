package com.example.taskmanager.data

import androidx.room.*
import com.example.taskmanager.domain.Task
import java.util.*

@Dao
interface TaskDao {

    @Insert
    fun addTask(task: TaskTable)

    @Delete
    fun deleteTask(task: TaskTable)

    @Update
    fun updateTask(task: TaskTable)

    @Query("SELECT max(id) FROM tasks")
    fun getLastTaskId(): Int

    @Query("UPDATE tasks SET isOutdated = :isOutdated WHERE taskDate < :today")
    fun updateOutdatedTasks(today: GregorianCalendar, isOutdated: Boolean = OUTDATED)

    @Query("SELECT * FROM tasks WHERE taskDate == :gregorianCalendar")
    fun getTasksByDay(gregorianCalendar: GregorianCalendar): List<TaskTable>

    @Query("SELECT * FROM tasks WHERE taskDate >= :start AND taskDate < :end")
    fun getTasksInInterval(start: GregorianCalendar, end: GregorianCalendar): List<TaskTable>

    @Query("SELECT taskDate FROM tasks WHERE taskDate >= :start AND taskDate < :end")
    fun getTasksDates(start: GregorianCalendar, end: GregorianCalendar): List<Long>

    companion object {
        private const val OUTDATED = true
    }
}