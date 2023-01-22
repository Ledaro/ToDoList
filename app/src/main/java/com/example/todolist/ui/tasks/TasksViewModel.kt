package com.example.todolist.ui.tasks

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.todolist.data.PreferencesManager
import com.example.todolist.data.SortOrder
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TasksViewModel @AssistedInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager,
    @Assisted state: SavedStateHandle
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

    val preferencesFlow = preferencesManager.preferencesFlow

    val searchQuery = MutableStateFlow("")

    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }
        .flatMapLatest { (query, filterPreferences) ->
            taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
        }

    val tasks = tasksFlow.asLiveData()

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedSelected(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onTaskSelected(task: Task) {

    }

    fun onTaskCheckedChange(task: Task, isChecked: Boolean) = viewModelScope.launch {
        taskDao.update(task.copy(completed = isChecked))
    }
}
