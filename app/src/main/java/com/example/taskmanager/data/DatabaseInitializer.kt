package com.example.taskmanager.data

import android.content.Context

class DatabaseInitializer {

    companion object {
        fun passContext(context: Context) {
            TaskDatabase.getDatabase(context)
        }
    }
}