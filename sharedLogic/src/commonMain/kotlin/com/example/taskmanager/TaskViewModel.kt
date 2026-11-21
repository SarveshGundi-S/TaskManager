package com.example.taskmanager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

final class TaskViewModel(
    private val repository: TaskRepository,
    private val navigationChannel: NavigationChannel,
) {
    private val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    val navigationEvents = navigationChannel.events

    fun fetchTasks() {
        viewModelScope.launch {
            executeOperation {
                repository.getTasks()
            }.onSuccess { tasks ->
                _uiState.value =
                    uiState.value.copy(
                        tasks = tasks,
                    )
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            executeOperation("Task added successfully") {
                repository.addTask(task)
            }.onSuccess {
                refreshTasks()
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            executeOperation("Task updated successfully") {
                repository.updateTask(task)
            }.onSuccess {
                refreshTasks()
            }
        }
    }

    val filteredTasks: List<Task>
        get() {
            val state = _uiState.value
            return when (state.selectedFilter) {
                TaskFilter.ALL -> state.tasks
                TaskFilter.PENDING -> state.tasks.filter { !it.isCompleted }
                TaskFilter.COMPLETED -> state.tasks.filter { it.isCompleted }
            }
        }

    fun updateFilter(filter: TaskFilter) {
        _uiState.update { currentState ->
            currentState.copy(selectedFilter = filter)
        }
    }

    fun toggleTaskCompletion(taskId: Int) {
        viewModelScope.launch {
            executeOperation("Task completed successfully") {
                repository.toggleTaskCompletion(taskId)
            }.onSuccess {
                refreshTasks()
            }
        }
    }

    fun delete(taskId: Int) {
        viewModelScope.launch {
            executeOperation("Task deleted successfully") {
                repository.deleteTask(taskId)
            }.onSuccess {
                refreshTasks()
            }
        }
    }

    fun requestDeleteConfirmation(taskId: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteConfirmationVisible = true,
                taskToDeleteById = taskId,
            )
        }
    }

    fun dismissDeleteConfirmation() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteConfirmationVisible = false,
                taskToDeleteById = null,
            )
        }
    }

    fun confirmDelete() {
        val taskId = _uiState.value.taskToDeleteById ?: return

        dismissDeleteConfirmation()
        delete(taskId)
    }

    private suspend fun refreshTasks() {
        val tasks = repository.getTasks()

        _uiState.value =
            _uiState.value.copy(
                tasks = tasks,
            )
    }

    private suspend fun <T> executeOperation(
        successMessage: String? = null,
        operation: suspend () -> T,
    ): Result<T> {
        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null,
                successMessage = null,
            )

        return try {
            val result = operation()

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    successMessage = successMessage,
                )

            Result.success(result)
        } catch (exception: Exception) {
            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.message ?: "Something went wrong",
                )

            Result.failure(exception)
        }
    }

    fun navigateToAddTask() {
        viewModelScope.launch {
            navigationChannel.send(
                NavigationEvent.NavigateToAddTask,
            )
        }
    }

    fun navigateToEditTask(taskId: Int) {
        viewModelScope.launch {
            navigationChannel.send(
                NavigationEvent.NavigateToEditTask(taskId),
            )
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            navigationChannel.send(
                NavigationEvent.NavigateBack,
            )
        }
    }
}
