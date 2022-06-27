package com.example.taskmanager.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.TaskManagerRepositoryImpl
import com.example.taskmanager.domain.PriorityTypes
import com.example.taskmanager.domain.Task
import com.example.taskmanager.domain.usecases.AddTaskUseCase
import com.example.taskmanager.domain.usecases.EditTaskUseCase
import com.example.taskmanager.domain.usecases.GetTaskUseCase
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel : ViewModel() {
    private val repository = TaskManagerRepositoryImpl
    private val addToDoItemUseCase = AddTaskUseCase(repository)
    private val editToDoItemUseCase = EditTaskUseCase(repository)
    private val getToDoItemUseCase = GetTaskUseCase(repository)

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    private val _errorInputText = MutableLiveData<Boolean>()
    val errorInputText: LiveData<Boolean>
        get() = _errorInputText

    private val _errorInputDate = MutableLiveData<Boolean>()
    val errorInputDate: LiveData<Boolean>
        get() = _errorInputDate

    private val _errorInputPriority = MutableLiveData<Boolean>()
    val errorInputPriority: LiveData<Boolean>
        get() = _errorInputPriority

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    val priorities: Array<String> = arrayOf("Низкий", "Средний", "Высокий")

    fun setToDoTask(id: Int){
        val task = getToDoItemUseCase.getTask(id)
        _task.value = task
    }

    fun addToDoItem(taskName: String?,
                    taskDescription: String?,
                    taskDate: String?,
                    priority: String?) {
        val parsedName = parseTaskText(taskName)
        val parsedDescription = parseTaskText(taskDescription)
        val parsedDate = parseTaskDate(taskDate)
        val parsedPriority = parseTaskPriority(priority)
        val isFieldsFilledCorrect = validateInput(parsedName, parsedDate)
        if(isFieldsFilledCorrect){
            val item = Task(taskName = parsedName,
                taskDescription = parsedDescription,
                priorityType = parsedPriority,
                taskDate = parsedDate)
            addToDoItemUseCase.addTask(item)
            finishWork()
        }
    }

    fun editToDoItem(taskName: String?,
                     taskDescription: String?,
                     taskDate: String?,
                     priority: String?) {
        val parsedName = parseTaskText(taskName)
        val parsedDescription = parseTaskText(taskDescription)
        val parsedDate = parseTaskDate(taskDate)
        val parsedPriority = parseTaskPriority(priority)
        val isFieldsFilledCorrect = validateInput(parsedName, parsedDate)
        if(isFieldsFilledCorrect){
            _task.value?.let {
                val task = it.copy(taskName = parsedName,
                    taskDescription = parsedDescription,
                    priorityType = parsedPriority,
                    taskDate = parsedDate)

                editToDoItemUseCase.editTask(task)
                finishWork()
            }
        }
    }

    fun getCurrentPriorityIndex(): Int{
        val priorityIndex = when(_task.value?.priorityType){
            PriorityTypes.LOW_PRIORITY -> 0
            PriorityTypes.MEDIUM_PRIORITY -> 1
            PriorityTypes.HIGH_PRIORITY -> 2
            else -> throw RuntimeException("Priority index cannot be found")
        }
        return priorityIndex
    }

    private fun parseTaskText(taskText: String?) : String{
        return taskText?.trim() ?: ""
    }

    private fun parseTaskDate(taskDate: String?) : GregorianCalendar {
        val parsedString = taskDate?.trim()?.filterNot {
            it == '.' || it == '/' || it == '-'
        }
        val date = GregorianCalendar()
        try {
            date.time = SimpleDateFormat("ddMMyyyy", Locale.ROOT).parse(parsedString)
        }
        catch (e: Exception){
            return defaultDate
        }
        return date
    }

    private fun parseTaskPriority(priority: String?) : PriorityTypes {
        val priorityType = when(priority) {
            "Низкий" -> PriorityTypes.LOW_PRIORITY
            "Средний" -> PriorityTypes.MEDIUM_PRIORITY
            "Высокий" -> PriorityTypes.HIGH_PRIORITY
            else -> {
                throw RuntimeException("Priority type is not matched")
            }
        }
        return priorityType
    }

    private fun validateInput(taskText: String, taskDate: GregorianCalendar): Boolean{
        var isFieldsFilledCorrect = true
        if(taskDate == defaultDate){
            _errorInputDate.value = true
            isFieldsFilledCorrect = false
        }
        val today = GregorianCalendar(
            GregorianCalendar.YEAR,
            GregorianCalendar.MONTH,
            GregorianCalendar.DAY_OF_MONTH
        )
        if(taskDate != defaultDate && taskDate < today){
            _errorInputDate.value = true
            isFieldsFilledCorrect = false
        }
        if(taskText.isBlank()){
            _errorInputText.value = true
            isFieldsFilledCorrect = false
        }
        return isFieldsFilledCorrect
    }

    fun resetErrorInputText() {
        _errorInputText.value = false
    }

    fun resetErrorInputDate() {
        _errorInputDate.value = false
    }

    fun resetErrorInputPriority() {
        _errorInputPriority.value = false
    }

    private fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    companion object {
        private val defaultDate = GregorianCalendar(2000,1,1)
    }
}