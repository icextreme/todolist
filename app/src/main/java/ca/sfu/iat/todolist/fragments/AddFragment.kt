package ca.sfu.iat.todolist.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.sfu.iat.todolist.ToDoListApplication
import ca.sfu.iat.todolist.database.Task
import ca.sfu.iat.todolist.databinding.FragmentAddBinding
import ca.sfu.iat.todolist.viewmodel.TaskViewModel
import ca.sfu.iat.todolist.viewmodel.TaskViewModelFactory

class AddFragment : Fragment() {

    private val navigationArgs: AddFragmentArgs by navArgs()
    lateinit var task: Task

    private val viewModel: TaskViewModel by activityViewModels {
        TaskViewModelFactory(
            (activity?.application as ToDoListApplication).database.taskDao()
        )
    }

    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun isInputValid(): Boolean {
        return viewModel.isInputValid(
            binding.taskTitle.text.toString(),
            binding.taskDescription.text.toString(),
            binding.taskType.text.toString(),
        )
    }

    private fun addNewTask() {
        if (isInputValid()) {
            viewModel.addTask(
                binding.taskTitle.text.toString(),
                binding.taskDescription.text.toString(),
                binding.taskType.text.toString()
            )
        }
    }

    private fun updateTask() {
        if (isInputValid()) {
            viewModel.updateExistingTask(
                this.navigationArgs.itemId,
                binding.taskTitle.text.toString(),
                binding.taskDescription.text.toString(),
                binding.taskType.text.toString(),
                false
            )
            val action = AddFragmentDirections.actionAddFragmentToListFragment()
            findNavController().navigate(action)
        }
    }

    private fun bind(task: Task) {
        binding.apply {
            taskTitle.setText(task.taskTitle, TextView.BufferType.SPANNABLE)
            taskDescription.setText(task.taskDescription, TextView.BufferType.SPANNABLE)
            taskType.setText(task.taskType, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateTask() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.itemId
        if (id > 0) {
            viewModel.retrieveTask(id).observe(this.viewLifecycleOwner) { result ->
                task = result
                bind(task)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewTask()
                findNavController().navigateUp()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}