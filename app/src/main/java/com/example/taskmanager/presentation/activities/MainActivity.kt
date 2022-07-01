package com.example.taskmanager.presentation.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.data.DatabaseInitializer
import com.example.taskmanager.presentation.TaskAdapter
import com.example.taskmanager.presentation.viewmodels.MainViewModel
import com.example.taskmanager.presentation.viewmodels.TaskListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    private lateinit var buttonAddTask: FloatingActionButton
    private lateinit var rvTasksList: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DatabaseInitializer.passContext(this)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupSlideMenu()
        parseIntent()
        initViews()
        setupRecyclerView()
        setupSwipeListener()
        setupAddButton()
        viewModel.tasks.observe(this){
            adapter.taskList = it
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews(){
        buttonAddTask = findViewById(R.id.button_add_task)
        rvTasksList = findViewById(R.id.rv_tasks_list)
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

    private fun setupSlideMenu() {
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this,
            drawerLayout,
            R.string.open,
            R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_today_tasks -> {
                    val intent = newIntent(this,false)
                    startActivity(intent)
                }
                R.id.nav_unfinished_tasks -> {
                    val intent = newIntent(this,true)
                    startActivity(intent)
                }
                else -> TODO()
            }
            true
        }
    }

    private fun parseIntent() {
        if(!intent.hasExtra(EXTRA_MODE)){
            viewModel.getTodayTasks()
            return
        }
        val extraMode = intent.getStringExtra(EXTRA_MODE)
        if(extraMode != MODE_DATE && extraMode != MODE_OUTDATED && extraMode != MODE_TODAY){
            throw RuntimeException("Mode don't found")
        }
        if(extraMode == MODE_DATE){
            val dateL = intent.getLongExtra(EXTRA_DATE, DEFAULT_DATE)
            viewModel.getTasksByDay(dateL)
        }
        if(extraMode == MODE_TODAY){
            viewModel.getTodayTasks()
        }
        if(extraMode == MODE_OUTDATED){
            viewModel.getOutdatedTasks()
        }
    }

    private fun setupAddButton() {
        buttonAddTask.setOnClickListener {
            val intent = TaskActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    companion object {

        private const val EXTRA_DATE = "Date"
        private const val EXTRA_MODE = "Mode"
        private const val MODE_TODAY = "Mode today"
        private const val MODE_OUTDATED = "Mode outdated"
        private const val MODE_DATE = "Mode date"
        private const val DEFAULT_DATE = 0L

        fun newIntent(context: Context, isOutdated: Boolean) : Intent {
            val intent = Intent(context, MainActivity::class.java)
            if(isOutdated)
                intent.putExtra(EXTRA_MODE, MODE_OUTDATED)
            else
                intent.putExtra(EXTRA_MODE, MODE_TODAY)
            return intent
        }

        fun newIntent(context: Context, date: Long) : Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_MODE, MODE_DATE)
            intent.putExtra(EXTRA_DATE, date)
            return intent
        }
    }

    /*
    private fun launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.task_list_container, fragment)
            .addToBackStack(null)
            .commit()
    }

     */
}