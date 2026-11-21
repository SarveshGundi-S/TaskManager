package com.example.taskmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {

    private lateinit var container: TaskContainer
    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        container = TaskContainer()
        viewModel = TaskViewModel(container.taskRepository)

        setContent {
            TaskScreen(viewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        container.close()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    val container = TaskContainer()
    val viewModel = TaskViewModel(container.taskRepository)
//    App()
    TaskScreen(viewModel)
}