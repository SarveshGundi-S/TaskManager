package com.example.taskmanager

class TaskRepositoryImpl(
    private val localDataSource: TaskLocalDataSource,
) : TaskRepository {
    override suspend fun getTasks(): List<Task> = localDataSource.getTasks()

    override suspend fun addTask(task: Task) {
        localDataSource.addTask(task)
    }

    override suspend fun updateTask(task: Task) {
        localDataSource.updateTask(task)
    }

    override suspend fun deleteTask(taskId: Int) {
        localDataSource.deleteTask(taskId)
    }

    override suspend fun toggleTaskCompletion(taskId: Int) {
        localDataSource.completeTask(taskId)
    }
}
