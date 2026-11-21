package com.example.taskmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun TaskScreen(
    viewModel: TaskViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchTasks()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Text(text = "Task Manager")

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let { message ->
            Text(text = message)
        }

        LazyColumn {
            items(
                items = uiState.tasks,
                key = { it.id }
            ) { task ->
                Column {
                    Text(text = task.title)
                    Text(text = task.description)
                }
            }
        }
    }
}