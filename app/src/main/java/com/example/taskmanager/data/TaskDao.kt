package com.example.taskmanager.data

import androidx.room.*
import com.example.taskmanager.domain.PriorityTypes
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

    @Query("UPDATE tasks SET isOutdated = :isOutdated WHERE taskDate < :today AND NOT isDone")
    fun updateOutdatedTasks(today: GregorianCalendar, isOutdated: Boolean = OUTDATED)

    @Query("SELECT * FROM tasks WHERE taskDate == :gregorianCalendar")
    fun getTasksByDay(gregorianCalendar: GregorianCalendar): List<TaskTable>

    @Query("SELECT * FROM tasks WHERE taskDate >= :start AND taskDate < :end")
    fun getTasksInInterval(start: GregorianCalendar, end: GregorianCalendar): List<TaskTable>

    @Query("SELECT taskDate as dayL, count(*) as amountOfAllTasks, count(CASE WHEN isDone THEN 1 END) as amountOfCompletedTasks FROM tasks WHERE taskDate >= :start AND taskDate <= :end GROUP BY taskDate")
    fun getTasksDates(start: GregorianCalendar, end: GregorianCalendar): List<DayData>

    @Query("SELECT count(*) as amountOfAllTasks, count(CASE WHEN isDone THEN 1 END) as amountOfCompletedTasks, count(CASE WHEN priorityType = :highPriority THEN 1 END) as amountOfTasksWithHighPriority, count(CASE WHEN priorityType = :mediumPriority THEN 1 END) as amountOfTasksWithMediumPriority, count(CASE WHEN priorityType = :lowPriority THEN 1 END) as amountOfTasksWithLowPriority, count(CASE WHEN isOutdated THEN 1 END) as amountOfOutdatedTasks FROM tasks")
    fun getBasicStatistics(
        highPriority: PriorityTypes = PriorityTypes.HIGH_PRIORITY,
        mediumPriority: PriorityTypes = PriorityTypes.MEDIUM_PRIORITY,
        lowPriority: PriorityTypes = PriorityTypes.LOW_PRIORITY,
    ): BasicStatistics

    @Query("SELECT count(*) as amountOfTodayTasks, count(CASE WHEN isDone THEN 1 END) as amountOfTodayCompletedTasks FROM tasks WHERE taskDate = :gregorianCalendar")
    fun getTodayStatistics(gregorianCalendar: GregorianCalendar): TodayStatistics

    @Query("SELECT * FROM tasks WHERE isOutdated == :isOutdated AND NOT isDone")
    fun getOutdatedTasks(isOutdated: Boolean = OUTDATED): List<TaskTable>

    @Query("SELECT * FROM tasks")
    fun getTasks(): List<TaskTable>

    companion object {
        private const val OUTDATED = true
    }
}