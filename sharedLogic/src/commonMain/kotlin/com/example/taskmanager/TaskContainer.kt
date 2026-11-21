package com.example.taskmanager

class TaskContainer {
    private val realm = RealmDatabase.open()
    val taskLocalDataSource = TaskLocalDataSource(realm)
    val taskRepository: TaskRepository = TaskRepositoryImpl(taskLocalDataSource)

    val navigationChannel = NavigationChannel()

    fun close() {
        realm.close()
    }
}
