package com.example.todolist.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todolist.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_tasks) {

    @Inject
    lateinit var tasksViewModelFactory: TasksViewModel.TasksViewModelFactory

    private val viewModel: TasksViewModel by viewModels {
        TasksViewModel.provideFactory(tasksViewModelFactory, this, arguments)
    }
}