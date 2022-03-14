package ca.sfu.iat.todolist.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY title ASC")
    fun getTasksByTitleAsc(): Flow<List<Task>>

    @Query("SELECT * FROM task ORDER BY title DESC")
    fun getTasksByTitleDesc(): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE type LIKE :type")
    fun getTasksByType(type: String): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE id = :id ")
    fun getTask(id: Int): Flow<Task>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}