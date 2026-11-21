package com.example.taskmanager

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TaskExtensionsTest {
    private lateinit var tasksList: List<Task>

    @BeforeTest
    fun setup() {
        tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", false),
                Task(2, "Learn SwiftUI", "", true),
                Task(3, "Learn KMP", "", false),
            )
    }

    @Test
    fun shouldReturnCompletedTaskCount() {
        val result = tasksList.completedCount()
        assertEquals(1, result)
    }

    @Test
    fun shouldReturnTaskCountWhenAllTasksAreCompleted() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", true),
                Task(2, "Learn SwiftUI", "", true),
                Task(3, "Learn KMP", "", true),
            )
        val result = tasksList.completedCount()
        assertEquals(3, result)
    }

    @Test
    fun shouldReturnZeroWhenNoTasksAreCompleted() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", false),
                Task(2, "Learn SwiftUI", "", false),
                Task(3, "Learn KMP", "", false),
            )
        val result = tasksList.completedCount()
        assertEquals(0, result)
    }

    @Test
    fun shouldReturnZeroWhenTaskListIsEmpty() {
        val taskList = listOf<Task>()
        val result = taskList.completedCount()
        assertEquals(0, result)
    }

    @Test
    fun shouldReturnPendingTaskCount() {
        val result = tasksList.pendingCount()
        assertEquals(2, result)
    }

    @Test
    fun shouldReturnTaskCountWhenAllTasksArePending() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", false),
                Task(2, "Learn SwiftUI", "", false),
                Task(3, "Learn KMP", "", false),
            )
        val result = tasksList.pendingCount()
        assertEquals(3, result)
    }

    @Test
    fun shouldReturnZeroWhenNoTasksArePending() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", true),
                Task(2, "Learn SwiftUI", "", true),
                Task(3, "Learn KMP", "", true),
            )
        val result = tasksList.pendingCount()
        assertEquals(0, result)
    }

    @Test
    fun shouldReturnZeroWhenPendingTaskListIsEmpty() {
        val taskList = listOf<Task>()
        val result = taskList.pendingCount()
        assertEquals(0, result)
    }

    @Test
    fun shouldReturnPendingTask() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", false),
                Task(2, "Learn SwiftUI", "", true),
                Task(3, "Learn KMP", "", true),
            )
        val result = tasksList.hasPendingTasks()
        assertTrue { result }
    }

    @Test
    fun shouldReturnFalseWhenAllTasksAreCompleted() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", true),
                Task(2, "Learn SwiftUI", "", true),
                Task(3, "Learn KMP", "", true),
            )
        val result = tasksList.hasPendingTasks()
        assertFalse { result }
    }

    @Test
    fun shouldReturnTrueWhenTaskListEmpty() {
        val tasksList = listOf<Task>()
        val result = tasksList.hasPendingTasks()
        assertFalse { result }
    }

    @Test
    fun shouldReturnAllTasksTitles() {
        val tasksList =
            listOf(
                Task(1, "Learn Kotlin", "", false),
                Task(2, "Learn SwiftUI", "", false),
                Task(3, "Learn KMP", "", false),
            )
        val extpected =
            listOf(
                "Learn Kotlin",
                "Learn SwiftUI",
                "Learn KMP",
            )
        val result: List<String> = tasksList.taskTitles()
        assertContentEquals(extpected, result)
    }

    @Test
    fun shouldReturnEmptyWhenTaskListIsEmpty() {
        val tasksList = listOf<Task>()
        val expected = listOf<String>()
        val result: List<String> = tasksList.taskTitles()
        assertContentEquals(expected, result)
    }
}
