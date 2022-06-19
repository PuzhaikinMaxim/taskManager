package com.example.taskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.lang.RuntimeException

@Database(entities = [TaskTable::class], version = 1, exportSchema = false)
@TypeConverters(GregorianCalendarConverter::class)
abstract class TaskDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase{
            val tempDatabase = INSTANCE
            if(tempDatabase != null){
                return tempDatabase
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database.db"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                return instance
            }
        }

        fun getDatabase(): TaskDatabase{
            val tempDatabase = INSTANCE
            if(tempDatabase != null){
                return tempDatabase
            }
            else {
                throw RuntimeException("To be corrected")
            }
        }
    }
}