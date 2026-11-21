package com.example.taskmanager

val TaskViewModel.getTaskViewModel: CFlow<TaskUiState>
    get() = uiState.asCFlow()

val TaskViewModel.getNavigationEvents: CFlow<NavigationEvent>
    get() = navigationEvents.asCFlow()
