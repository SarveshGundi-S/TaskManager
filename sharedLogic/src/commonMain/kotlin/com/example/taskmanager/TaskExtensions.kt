package com.example.taskmanager

fun List<Task>.pendingCount(): Int = this.count { !it.isCompleted }

fun List<Task>.completedCount(): Int = this.count { it.isCompleted }

fun List<Task>.hasPendingTasks(): Boolean {
//    return this.pendingCount() > 0
    return this.any { !it.isCompleted }
}

fun List<Task>.taskTitles(): List<String> = this.map { it.title }
