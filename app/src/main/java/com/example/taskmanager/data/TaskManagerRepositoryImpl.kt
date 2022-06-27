package com.example.taskmanager.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.TaskManagerRepository
import java.lang.RuntimeException
import java.util.*

object TaskManagerRepositoryImpl : TaskManagerRepository {

    private val tasksList = sortedSetOf<Task>({
            o1,o2 -> o1.id.compareTo(o2.id)
    })
    private val tasksListLD = MutableLiveData<List<Task>>()
    private val datesListLD = MutableLiveData<List<GregorianCalendar>>()
    private val taskDatabase = TaskDatabase.getDatabase()
    private var autoIncrementId = 0

    init {
        autoIncrementId = taskDatabase.taskDao().getLastTaskId()+1
        val today = GregorianCalendar()
        taskDatabase.taskDao().updateOutdatedTasks(today)
        val list = taskDatabase.taskDao().getTasks()
        println("-----------------------------------------")
        for(elem in list){
            //println(elem.toString())
        }
        updateList()
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
        println("-----------------------------------------")
        println(date.toString())
        var cnt = 0
        for(item in taskTableList){
            println(++cnt)
            println(item.toString())
            tasksList.add(TaskConverter.convertFromTable(item))
        }
        println(tasksList)
        updateList()
        return tasksListLD
    }

    override fun getTaskDatesList(start: GregorianCalendar,
                         end: GregorianCalendar): LiveData<List<GregorianCalendar>> {
        val datesList = mutableListOf<GregorianCalendar>()
        val datesInMillis = taskDatabase.taskDao().getTasksDates(start, end)
        for(dateMillis in datesInMillis){
            val date = GregorianCalendar()
            date.timeInMillis = dateMillis
            datesList.add(date)
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

    override fun getTask(id: Int): Task {
        return tasksList.find {
            it.id == id
        } ?: throw RuntimeException("Task with id $id is not found")
    }

    override fun addTask(task: Task) {
        task.id = autoIncrementId++
        tasksList.add(task)
        taskDatabase.taskDao().addTask(TaskConverter.convertToTable(task))
        updateList()
    }

    override fun editTask(task: Task) {
        val taskToDelete = getTask(task.id)
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

}