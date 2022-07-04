package com.example.taskmanager.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R
import com.example.taskmanager.domain.Task
import com.example.taskmanager.presentation.fragments.TaskFragment

class TaskActivity : AppCompatActivity(), TaskFragment.OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var toDoTaskId = Task.UNDEFINED_ID
    private var mainScreenMode = MODE_UNKNOWN
    private var mainSelectedDate = DEFAULT_DATE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        parseIntent()
        if(savedInstanceState == null) {
            initRightMode()
        }
    }

    private fun initRightMode(){
        val fragment = when(screenMode) {
            MODE_EDIT -> TaskFragment.newInstanceEditItem(toDoTaskId)
            MODE_ADD -> TaskFragment.newInstanceAddItem()
            else -> throw RuntimeException("Unknown screen mode $screenMode")
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.task_item_container, fragment)
            .commit()
    }

    private fun parseIntent() {
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT){
            if(!intent.hasExtra(EXTRA_TASK_ITEM_ID)){
                throw RuntimeException("Param task item id is absent")
            }
            toDoTaskId = intent.getIntExtra(EXTRA_TASK_ITEM_ID, Task.UNDEFINED_ID)
        }
        if(!intent.hasExtra(PREVIOUS_EXTRA_MODE) || !intent.hasExtra(PREVIOUS_EXTRA_DATE))
                throw RuntimeException("Previous mode and date are missing")
        mainScreenMode = intent.getStringExtra(PREVIOUS_EXTRA_MODE)!!
        println(mainScreenMode)
        mainSelectedDate = intent.getLongExtra(PREVIOUS_EXTRA_DATE, DEFAULT_DATE)
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val EXTRA_TASK_ITEM_ID = "extra_task_item_id"
        private const val MODE_UNKNOWN = "mode_unknown"
        private const val DEFAULT_DATE = 0L
        private const val PREVIOUS_EXTRA_MODE = "previous_mode"
        private const val PREVIOUS_EXTRA_DATE = "previous_date"

        fun newIntentEditItem(context: Context,
                              id: Int,
                              extraScreenMode: String,
                              extraDate: Long
        ) : Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_TASK_ITEM_ID, id)
            intent.putExtra(PREVIOUS_EXTRA_MODE, extraScreenMode)
            intent.putExtra(PREVIOUS_EXTRA_DATE, extraDate)
            return intent
        }

        fun newIntentAddItem(context: Context,
                             extraScreenMode: String,
                             extraDate: Long) : Intent {
            val intent = Intent(context, TaskActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            intent.putExtra(PREVIOUS_EXTRA_MODE, extraScreenMode)
            intent.putExtra(PREVIOUS_EXTRA_DATE, extraDate)
            return intent
        }
    }

    override fun onEditingFinished() {
        val intent = MainActivity.newIntent(this, mainScreenMode, mainSelectedDate)
        startActivity(intent)
    }
}