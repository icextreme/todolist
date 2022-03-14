package ca.sfu.iat.todolist.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ca.sfu.iat.todolist.database.Task
import ca.sfu.iat.todolist.databinding.TodoItemBinding

class TaskListAdapter(private val onItemClicked: (Task) -> Unit) :
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            TodoItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val current = getItem(position)

        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }

        holder.bind(current)
    }

    class TaskViewHolder(private var binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                taskTitle.text = task.taskTitle
                taskDescription.text = task.taskDescription
                taskType.text = task.taskType
                if (task.taskCompleted) {
                    imageView.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldTask: Task, newTask: Task): Boolean {
                return oldTask === newTask
            }

            override fun areContentsTheSame(oldTask: Task, newTask: Task): Boolean {
                return oldTask.taskTitle == newTask.taskTitle
            }
        }
    }
}




