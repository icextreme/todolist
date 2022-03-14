package ca.sfu.iat.todolist.viewmodel

import androidx.lifecycle.*
import ca.sfu.iat.todolist.database.Task
import ca.sfu.iat.todolist.database.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    val allTasks: LiveData<List<Task>> = taskDao.getTasks().asLiveData()

    fun retrieveTask(id: Int): LiveData<Task> {
        return taskDao.getTask(id).asLiveData()
    }

    fun addTask(
        taskTitle: String,
        taskDescription: String,
        taskType: String
    ) {
        insertTask(
            createNewTask(
                taskTitle = taskTitle,
                taskDescription = taskDescription,
                taskType = taskType
            )
        )
    }

    private fun createNewTask(
        taskTitle: String,
        taskDescription: String,
        taskType: String
    ): Task {
        return Task(
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            taskType = taskType,
            taskCompleted = false
        )
    }

    private fun updateExistingTaskEntry(
        taskId: Int,
        taskTitle: String,
        taskDescription: String,
        taskType: String,
        taskCompleted: Boolean
    ): Task {
        return Task(
            id = taskId,
            taskTitle = taskTitle,
            taskDescription = taskDescription,
            taskType = taskType,
            taskCompleted = taskCompleted
        )
    }

    fun updateExistingTask(
        taskId: Int,
        taskTitle: String,
        taskDescription: String,
        taskType: String,
        taskCompleted: Boolean
    ) {
        updateTask(
            updateExistingTaskEntry(
                taskId,
                taskTitle,
                taskDescription,
                taskType,
                taskCompleted
            )
        )
    }

    private fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    private fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }

    fun isInputValid(title: String, description: String, type: String): Boolean {
        return !(title.isBlank() || description.isBlank() || type.isBlank())
    }
}

class TaskViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}