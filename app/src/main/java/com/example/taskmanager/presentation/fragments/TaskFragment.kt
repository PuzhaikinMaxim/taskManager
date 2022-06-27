package com.example.taskmanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.R
import com.example.taskmanager.domain.Task
import com.example.taskmanager.presentation.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

class TaskFragment : Fragment() {

    private lateinit var viewModel: TaskViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var tilName: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var tilDescription: TextInputLayout
    private lateinit var etDescription: EditText
    private lateinit var tilDate: TextInputLayout
    private lateinit var etDate: EditText
    private lateinit var prioritySpinner: Spinner
    private lateinit var saveButton: Button

    private var screenMode = MODE_UNKNOWN
    private var toDoTaskId = Task.UNDEFINED_ID


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        } else {
            throw RuntimeException("Activity must implement listener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        initViews(view)
        initRightMode()
        observeViewModel()
        addOnTextChangedListener()
    }

    private fun launchAddMode() {
        saveButton.setOnClickListener {
            viewModel.addToDoItem(
                etName.text?.toString(),
                etDescription.text?.toString(),
                etDate.text?.toString(),
                prioritySpinner.selectedItem?.toString()
            )
        }
    }

    private fun launchEditMode() {
        viewModel.setToDoTask(toDoTaskId)
        viewModel.task.observe(viewLifecycleOwner){
            etName.setText(it.taskName)

            val dateString = dateToString(it.taskDate)
            etDate.setText(dateString)
        }
        saveButton.setOnClickListener {
            viewModel.editToDoItem(
                etName.text?.toString(),
                etDescription.text?.toString(),
                etDate.text?.toString(),
                prioritySpinner.selectedItem?.toString()
            )
        }
        prioritySpinner.setSelection(viewModel.getCurrentPriorityIndex())
    }

    private fun observeViewModel() {
        viewModel.errorInputText.observe(viewLifecycleOwner) {
            val message = if(it) {
                "Неверное название задачи"
            }
            else {
                null
            }
            tilName.error = message
        }
        viewModel.errorInputDate.observe(viewLifecycleOwner) {
            val message = if(it) {
                "Неверный формат даты"
            }
            else {
                null
            }
            tilDate.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
            //activity?.onBackPressed()
        }
    }

    private fun addOnTextChangedListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputText()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        etDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputDate()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun initRightMode(){
        when(screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }

    private fun dateToString(gCalendarDate: GregorianCalendar) : String {
        val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.ROOT)
        val date = gCalendarDate.time
        return dateFormatter.format(date)
    }

    private fun parseParams() {
        val args = requireArguments()
        if(!args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_EDIT && mode != MODE_ADD){
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if(screenMode == MODE_EDIT){
            if(!args.containsKey(TASK_ITEM_ID)){
                throw RuntimeException("Param task item id is absent")
            }
            toDoTaskId = args.getInt(TASK_ITEM_ID, Task.UNDEFINED_ID)
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_text)
        etName = view.findViewById(R.id.et_text)
        tilDescription = view.findViewById(R.id.til_description)
        etDescription = view.findViewById(R.id.et_description)
        tilDate = view.findViewById(R.id.til_date)
        etDate = view.findViewById(R.id.et_date)
        etDate.inputType = InputType.TYPE_DATETIME_VARIATION_DATE
        prioritySpinner = view.findViewById(R.id.priority_spinner)
        val arrayAdapter = ArrayAdapter(
            view.context,
            R.layout.support_simple_spinner_dropdown_item,
            viewModel.priorities
        )
        prioritySpinner.adapter = arrayAdapter
        saveButton = view.findViewById(R.id.save_button)
    }

    companion object {

        private const val SCREEN_MODE = "extra_mode"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val TASK_ITEM_ID = "extra_task_item_id"
        private const val MODE_UNKNOWN = "mode_unknown"

        fun newInstanceEditItem(id: Int) : TaskFragment {
            return TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(TASK_ITEM_ID, id)
                }
            }
        }

        fun newInstanceAddItem() : TaskFragment {
            return TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
    }

    interface OnEditingFinishedListener {

        fun onEditingFinished()
    }
}