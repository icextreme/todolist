package ca.sfu.iat.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.sfu.iat.todolist.R
import ca.sfu.iat.todolist.ToDoListApplication
import ca.sfu.iat.todolist.database.Task
import ca.sfu.iat.todolist.databinding.FragmentEditBinding
import ca.sfu.iat.todolist.viewmodel.TaskViewModel
import ca.sfu.iat.todolist.viewmodel.TaskViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class EditFragment : Fragment() {

    private val navigationArgs: EditFragmentArgs by navArgs()
    lateinit var task: Task

    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as ToDoListApplication).database.taskDao()
        )
    }
    private var _binding: FragmentEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(task: Task) {
        binding.apply {
            taskTitle.text = task.taskTitle
            taskDescription.text = task.taskDescription
            taskType.text = task.taskType
            completeTask.setOnClickListener {
                viewModel.updateExistingTask(
                    this@EditFragment.navigationArgs.itemId,
                    taskTitle.text.toString(),
                    taskDescription.text.toString(),
                    taskType.text.toString(),
                    taskCompleted = true,
                )
                findNavController().navigateUp()
            }

            editTask.setOnClickListener {
                editTask()
            }

            deleteTask.setOnClickListener {
                showConfirmationDialog()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId

        viewModel.retrieveTask(id).observe(this.viewLifecycleOwner) { result ->
            task = result
            bind(task)
        }
    }

    private fun editTask() {
        val action = EditFragmentDirections.actionEditFragmentToAddFragment(task.id)
        this.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteTask()
            }
            .show()
    }

    private fun deleteTask() {
        viewModel.deleteTask(task)
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}