package com.example.taskmanager

interface TaskRepository {
    suspend fun getTasks(): List<Task>

    suspend fun addTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(taskId: Int)

    suspend fun toggleTaskCompletion(taskId: Int)
}
