package com.example.taskmanager

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class TaskLocalDataSource(
    private val realm: Realm,
) {
    suspend fun addTask(task: Task) {
        realm.write {
            copyToRealm(
                RealmTask().apply {
                    id = task.id
                    title = task.title
                    description = task.description
                    isCompleted = task.isCompleted
                },
            )
        }
    }

    fun getTasks(): List<Task> =
        realm
            .query<RealmTask>()
            .find()
            .map { realmTask ->
                Task(
                    id = realmTask.id,
                    title = realmTask.title,
                    description = realmTask.description,
                    isCompleted = realmTask.isCompleted,
                )
            }

    suspend fun updateTask(task: Task) {
        realm.write {
            val existingTask = query<RealmTask>("id == $0", task.id).first().find()
            if (existingTask != null) {
                existingTask.title = task.title
                existingTask.description = task.description
                existingTask.isCompleted = task.isCompleted
            } else {
                println("Task Does not exist in DataBase")
            }
        }
    }

    suspend fun deleteTask(taskId: Int) {
        realm.write {
            val existingTask =
                query<RealmTask>("id == $0", taskId)
                    .first()
                    .find()
            if (existingTask != null) {
                delete(existingTask)
            }
        }
    }

    suspend fun completeTask(taskId: Int) {
        realm.write {
            val existingTask = query<RealmTask>("id == $0", taskId).first().find()
            if (existingTask != null) {
                existingTask.isCompleted = !existingTask.isCompleted
            }
        }
    }
}
