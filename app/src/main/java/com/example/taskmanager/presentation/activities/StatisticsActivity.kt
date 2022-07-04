package com.example.taskmanager.presentation.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.taskmanager.R
import com.example.taskmanager.presentation.viewmodels.StatisticsViewModel
import com.google.android.material.navigation.NavigationView
import java.lang.RuntimeException

class StatisticsActivity : AppCompatActivity() {

    private lateinit var statisticsViewModel: StatisticsViewModel
    private lateinit var tvAmountOfTasks: TextView
    private lateinit var tvAmountOfPriorityTasks: TextView
    private lateinit var tvAmountOfOutdatedTasks: TextView
    private lateinit var tvTodayTasksAmount: TextView
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        statisticsViewModel = ViewModelProvider(this).get(StatisticsViewModel::class.java)
        initViews()
        setTextViewText()
        setupSlideMenu()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
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
                    val intent = MainActivity.newIntent(this, false)
                    startActivity(intent)
                }
                R.id.nav_unfinished_tasks -> {
                    val intent = MainActivity.newIntent(this, true)
                    startActivity(intent)
                }
                R.id.nav_all_tasks -> {
                    val intent = ChooseDateActivity.newIntent(this)
                    startActivity(intent)
                }
                R.id.nav_statistics -> {
                    val intent = StatisticsActivity.newIntent(this)
                    startActivity(intent)
                }
                else -> throw RuntimeException("Раздел не найден")
            }
            true
        }
    }

    private fun initViews() {
        tvAmountOfTasks = findViewById(R.id.tv_amount_of_tasks)
        tvAmountOfPriorityTasks = findViewById(R.id.tv_amount_of_priority_tasks)
        tvAmountOfOutdatedTasks = findViewById(R.id.tv_amount_of_outdated_tasks)
        tvTodayTasksAmount = findViewById(R.id.tv_today_tasks_amount)
    }

    private fun setTextViewText(){
        statisticsViewModel.statistics.observe(this){
            tvAmountOfTasks.text = getString(
                R.string.amount_of_tasks,
                it.amountOfAllTasks.toString(),
                it.amountOfCompletedTasks.toString()
            )
            tvAmountOfPriorityTasks.text = getString(
                R.string.amount_priority_tasks,
                it.amountOfTasksWithHighPriority.toString(),
                it.amountOfTasksWithMediumPriority.toString(),
                it.amountOfTasksWithLowPriority.toString()
            )
            tvAmountOfOutdatedTasks.text = getString(
                R.string.outdated_tasks,
                it.amountOfOutdatedTasks.toString()
            )
            tvTodayTasksAmount.text = getString(
                R.string.today_task,
                it.amountOfTodayTasks.toString(),
                it.amountOfTodayCompletedTasks.toString()
            )
        }
    }

    companion object {

        fun newIntent(context: Context) : Intent {
            return Intent(context, StatisticsActivity::class.java)
        }
    }
}