package ca.sfu.iat.todolist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val taskTitle: String,
    @ColumnInfo(name = "description")
    val taskDescription: String,
    @ColumnInfo(name = "type")
    val taskType: String,
    @ColumnInfo(name = "completed")
    val taskCompleted: Boolean
)