package com.example.taskmanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.domain.Day
import com.example.taskmanager.domain.Statistics
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository
import java.lang.RuntimeException
import java.util.*

object TaskManagerRepositoryImpl : TaskManagerRepository {

    private val tasksList = sortedSetOf<Task>({
            o1,o2 -> o1.id.compareTo(o2.id)
    })
    private val tasksListLD = MutableLiveData<List<Task>>()
    private val datesListLD = MutableLiveData<List<Day>>()
    private val taskDatabase = TaskDatabase.getDatabase()
    private var autoIncrementId = 0

    init {
        autoIncrementId = taskDatabase.taskDao().getLastTaskId()+1
        taskDatabase.taskDao().updateOutdatedTasks(getTodayDate())
        val dayData = taskDatabase.taskDao().getTasksDates(
            GregorianCalendar(2000,1,1),
            GregorianCalendar(2030,1,1)
        )
        for(data in dayData){
            println(data)
        }
        val d = taskDatabase.taskDao().getTasks()
    }

    override fun getTasksList(): LiveData<List<Task>> {
        return tasksListLD
    }

    override fun getTasksList(start: GregorianCalendar,
                              end: GregorianCalendar): LiveData<List<Task>> {
        tasksList.clear()
        val taskTableList = taskDatabase.taskDao().getTasksInInterval(start, end)
        for(item in taskTableList){
            tasksList.add(TaskConverter.convertFromTable(item))
        }
        updateList()
        return tasksListLD
    }

    override fun getTasksList(date: GregorianCalendar) : LiveData<List<Task>> {
        tasksList.clear()
        val taskTableList = taskDatabase.taskDao().getTasksByDay(date)
        for(item in taskTableList){
            tasksList.add(TaskConverter.convertFromTable(item))
        }
        updateList()
        return tasksListLD
    }

    override fun getTaskDatesList(start: GregorianCalendar,
                         end: GregorianCalendar): LiveData<List<Day>> {
        val datesList = mutableListOf<Day>()
        for(i in 0 until start.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)){
            datesList.add(Day(i+1,false))
        }
        val datesData = taskDatabase.taskDao().getTasksDates(start, end)
        for(dateData in datesData){
            val date = GregorianCalendar()
            date.timeInMillis = dateData.dayL
            val dayOfMonth = date.get(GregorianCalendar.DAY_OF_MONTH)

            var isEveryTaskDone = false
            if(dateData.amountOfAllTasks == dateData.amountOfCompletedTasks)
                isEveryTaskDone = true

            val day = Day(dayOfMonth,isEveryTaskDone,false)
            datesList[dayOfMonth-1] = day
        }
        datesListLD.value = datesList
        return datesListLD
    }

    override fun getOutdatedTasks(): LiveData<List<Task>>{
        tasksList.clear()
        val taskTableList = taskDatabase.taskDao().getOutdatedTasks()
        for(item in taskTableList){
            tasksList.add(TaskConverter.convertFromTable(item))
        }
        updateList()
        return tasksListLD
    }

    override fun getStatistics(): LiveData<Statistics> {
        val basicStatistics = taskDatabase.taskDao().getBasicStatistics()
        val todayStatistics = taskDatabase.taskDao().getTodayStatistics(getTodayDate())
        val statistics = Statistics(
            basicStatistics.amountOfAllTasks,
            basicStatistics.amountOfCompletedTasks,
            basicStatistics.amountOfTasksWithHighPriority,
            basicStatistics.amountOfTasksWithMediumPriority,
            basicStatistics.amountOfTasksWithLowPriority,
            basicStatistics.amountOfOutdatedTasks,
            todayStatistics.amountOfTodayTasks,
            todayStatistics.amountOfTodayCompletedTasks
        )
        return MutableLiveData(statistics)
    }

    override fun getTask(id: Int): Task {
        return tasksList.find {
            it.id == id
        } ?: throw RuntimeException("Task with id $id is not found")
    }

    override fun addTask(task: Task) {
        task.id = autoIncrementId++
        if(task.taskDate < getTodayDate())
            task.isOutdated = true
        tasksList.add(task)
        taskDatabase.taskDao().addTask(TaskConverter.convertToTable(task))
        updateList()
    }

    override fun editTask(task: Task) {
        val taskToDelete = getTask(task.id)
        task.isOutdated = task.taskDate < getTodayDate()
        tasksList.remove(taskToDelete)
        tasksList.add(task)
        taskDatabase.taskDao().updateTask(TaskConverter.convertToTable(task))
        updateList()
    }

    override fun removeTask(id: Int) {
        val task = getTask(id)
        tasksList.remove(task)
        taskDatabase.taskDao().deleteTask(TaskConverter.convertToTable(task))
        updateList()
    }

    private fun updateList() {
        tasksListLD.value = tasksList.toList()
    }

    private fun getTodayDate() : GregorianCalendar{
        val today = GregorianCalendar()
        return GregorianCalendar(
            today.get(GregorianCalendar.YEAR),
            today.get(GregorianCalendar.MONTH),
            today.get(GregorianCalendar.DAY_OF_MONTH)
        )
    }
}