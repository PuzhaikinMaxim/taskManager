package com.example.taskmanager.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.TaskManagerRepositoryImpl
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.usecases.*
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    private val repository = TaskManagerRepositoryImpl

    private val getTasksListByDayUseCase = GetTasksListByDayUseCase(repository)
    private val removeTaskUseCase = RemoveTaskUseCase(repository)
    private val editTaskUseCase = EditTaskUseCase(repository)
    private val getTasksListUseCase = GetTasksListUseCase(repository)
    private val getOutdatedTasksUseCase = GetOutdatedTasksUseCase(repository)

    private val _header = MutableLiveData<String>()
    val header: LiveData<String>
        get() = _header

    val tasks = getTasksListUseCase.getTasksList()

    fun removeTask(task: Task){
        removeTaskUseCase.removeTask(task.id)
    }

    fun getTasksByDay(day: Long) {
        val gregDay = GregorianCalendar()
        gregDay.timeInMillis = day
        getTasksListByDayUseCase.getTasksListByDay(gregDay)
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        _header.value = dateFormatter.format(gregDay.time)
    }

    fun getTodayTasks() {
        val today = getTodayDate()
        getTasksListByDayUseCase.getTasksListByDay(today)
    }

    fun getOutdatedTasks() {
        getOutdatedTasksUseCase.getOutdatedTasks()
    }

    fun changeReadyState(task: Task) {
        val newTask = task.copy(isDone = !task.isDone)
        editTaskUseCase.editTask(newTask)
    }

    init {
        val today = getTodayDate()
        getTasksListByDayUseCase.getTasksListByDay(today)
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