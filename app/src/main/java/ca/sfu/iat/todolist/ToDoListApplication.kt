package ca.sfu.iat.todolist

import android.app.Application
import ca.sfu.iat.todolist.database.AppDatabase

class ToDoListApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}