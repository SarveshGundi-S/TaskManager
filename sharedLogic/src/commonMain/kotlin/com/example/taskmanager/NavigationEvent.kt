package com.example.taskmanager

sealed interface NavigationEvent {
    data object NavigateToTaskList : NavigationEvent

    data object NavigateToAddTask : NavigationEvent

    data class NavigateToEditTask(
        val taskId: Int,
    ) : NavigationEvent

    data object NavigateBack : NavigationEvent
}
