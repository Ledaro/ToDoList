package com.example.todolist.ui.tasks

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.savedstate.SavedStateRegistryOwner
import com.example.todolist.data.TaskDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TasksViewModel @AssistedInject constructor(
    private val taskDao: TaskDao,
    @Assisted state:SavedStateHandle
) : ViewModel() {

    @AssistedFactory
    interface TasksViewModelFactory {
        fun create(handle: SavedStateHandle): TasksViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: TasksViewModelFactory,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return assistedFactory.create(handle) as T
                }
            }
    }

    val tasks = taskDao.getTasks().asLiveData()
}