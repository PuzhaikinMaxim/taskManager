package com.example.taskmanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.presentation.TaskAdapter
import com.example.taskmanager.presentation.viewmodels.TaskListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListFragment : Fragment() {

    private lateinit var buttonAddTask: FloatingActionButton
    private lateinit var rvTasksList: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: TaskListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tasks_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerView()
        setupAddButton()
        setupSwipeListener()
        viewModel = ViewModelProvider(this).get(TaskListViewModel::class.java)
    }

    private fun initViews(view: View){
        buttonAddTask = view.findViewById(R.id.button_add_task)
        rvTasksList = view.findViewById(R.id.rv_tasks_list)
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter()
        rvTasksList.adapter = adapter
    }

    private fun setupSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.taskList[viewHolder.adapterPosition]
                viewModel.removeTask(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTasksList)
    }

    private fun setupAddButton() {

    }
}