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

    @Query("SELECT taskDate as dayL, count(*) as amountOfAllTasks, count(isDone) as amountOfCompletedTasks FROM tasks WHERE taskDate >= :start AND taskDate <= :end GROUP BY taskDate")
    fun getTasksDates(start: GregorianCalendar, end: GregorianCalendar): List<DayData>

    @Query("SELECT * FROM tasks WHERE isOutdated == :isOutdated")
    fun getOutdatedTasks(isOutdated: Boolean = OUTDATED): List<TaskTable>

    @Query("SELECT * FROM tasks")
    fun getTasks(): List<TaskTable>

    companion object {
        private const val OUTDATED = true
    }
}