package com.example.taskmanager.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskmanager.data.TaskManagerRepositoryImpl
import com.example.taskmanager.domain.Day
import com.example.taskmanager.domain.usecases.GetTaskDatesListUseCase
import java.util.*

class ChooseDateViewModel : ViewModel() {

    private val repository = TaskManagerRepositoryImpl
    private val getTaskDatesListUseCase = GetTaskDatesListUseCase(repository)
    private val _currentMonth = MutableLiveData<Int>()
    val currentMonth: LiveData<Int>
        get() = _currentMonth
    private val _currentYear = MutableLiveData<Int>()
    val currentYear: LiveData<Int>
        get() = _currentYear

    val datesList: LiveData<List<Day>>

    init {
        val minDate = getMinDate(getTodayDate())
        val maxDate = getMaxDate(getTodayDate())
        _currentMonth.value = getTodayDate().get(GregorianCalendar.MONTH)
        _currentYear.value = getTodayDate().get(GregorianCalendar.YEAR)
        datesList = getTaskDatesListUseCase.getTaskDatesList(minDate, maxDate)
    }

    private fun setDatesList(){
        val date = GregorianCalendar(_currentYear.value!!, _currentMonth.value!!, 1)
        val minDate = getMinDate(date)
        val maxDate = getMaxDate(date)
        getTaskDatesListUseCase.getTaskDatesList(minDate, maxDate)
    }

    fun handleMonthChange(changeBy: Int){
        if(_currentMonth.value!! + changeBy < MIN_MONTH){
            _currentMonth.value = MAX_MONTH
            _currentYear.value = _currentYear.value!! - 1
        }
        else if(_currentMonth.value!! + changeBy > MAX_MONTH){
            _currentMonth.value = MIN_MONTH
            _currentYear.value = _currentYear.value!! + 1
        }
        else {
            _currentMonth.value = _currentMonth.value!! + changeBy
        }
        setDatesList()
    }

    private fun getTodayDate() : GregorianCalendar {
        val today = GregorianCalendar()
        return GregorianCalendar(
            today.get(GregorianCalendar.YEAR),
            today.get(GregorianCalendar.MONTH),
            today.get(GregorianCalendar.DAY_OF_MONTH)
        )
    }

    private fun getMinDate(date: GregorianCalendar) : GregorianCalendar {
        return GregorianCalendar(
            date.get(GregorianCalendar.YEAR),
            date.get(GregorianCalendar.MONTH),
            date.getMinimum(GregorianCalendar.DAY_OF_MONTH)
        )
    }

    private fun getMaxDate(date: GregorianCalendar) : GregorianCalendar {
        return GregorianCalendar(
            date.get(GregorianCalendar.YEAR),
            date.get(GregorianCalendar.MONTH),
            date.getMaximum(GregorianCalendar.DAY_OF_MONTH)
        )
    }

    companion object {
        private const val MIN_MONTH = 0
        private const val MAX_MONTH = 11
    }
}