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
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.presentation.DaysListAdapter
import com.example.taskmanager.presentation.viewmodels.ChooseDateViewModel
import com.google.android.material.navigation.NavigationView
import java.lang.RuntimeException
import java.util.*

class ChooseDateActivity : AppCompatActivity() {

    private lateinit var tvYear: TextView
    private lateinit var tvMonth: TextView
    private lateinit var tvForward: TextView
    private lateinit var tvBack: TextView
    private lateinit var chooseDateViewModel: ChooseDateViewModel
    private lateinit var rvDaysList: RecyclerView
    private lateinit var daysListAdapter: DaysListAdapter
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var months: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_day)
        chooseDateViewModel = ViewModelProvider(this).get(ChooseDateViewModel::class.java)
        months = resources.getStringArray(R.array.month_names)
        setupButtons()
        setupTextViews()
        setupRecyclerView()
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
        drawerLayout = findViewById(R.id.drawer_layout_c)
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
                    val intent = newIntent(this)
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

    private fun setupRecyclerView() {
        rvDaysList = findViewById(R.id.rv_days_list)
        daysListAdapter = DaysListAdapter()
        rvDaysList.adapter = daysListAdapter
        chooseDateViewModel.datesList.observe(this){
            daysListAdapter.daysList = it
        }
        daysListAdapter.onDayItemListener = {
            val day = GregorianCalendar(
                chooseDateViewModel.currentYear.value!!,
                chooseDateViewModel.currentMonth.value!!,
                it.dayOfMonth
            )
            val intent = MainActivity.newIntent(this,day.timeInMillis)
            startActivity(intent)
        }
    }

    private fun setupTextViews() {
        tvYear = findViewById(R.id.tv_year)
        tvMonth = findViewById(R.id.tv_month)
        chooseDateViewModel.currentMonth.observe(this){
            tvMonth.text = months[it]
        }
        chooseDateViewModel.currentYear.observe(this){
            tvYear.text = it.toString()
        }
    }

    private fun setupButtons() {
        tvForward = findViewById(R.id.tv_forward)
        tvBack = findViewById(R.id.tv_back)
        tvForward.setOnClickListener {
            chooseDateViewModel.handleMonthChange(1)
        }
        tvBack.setOnClickListener {
            chooseDateViewModel.handleMonthChange(-1)
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ChooseDateActivity::class.java)
        }
    }
}