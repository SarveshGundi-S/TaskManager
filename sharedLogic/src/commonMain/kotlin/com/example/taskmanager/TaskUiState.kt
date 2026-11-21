package com.example.taskmanager

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val selectedFilter: TaskFilter = TaskFilter.ALL,
    val isDeleteConfirmationVisible: Boolean = false,
    val taskToDeleteById: Int? = null,
)
