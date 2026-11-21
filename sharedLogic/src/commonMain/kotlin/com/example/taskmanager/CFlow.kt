package com.example.taskmanager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CFlow<T : Any>(
    private val origin: Flow<T>,
) {
    fun watch(onEach: (T) -> Unit): Closeable {
        val scope = CoroutineScope(Dispatchers.Main + Job())

        origin
            .onEach { value ->
                onEach(value)
            }.launchIn(scope)

        return Closeable {
            scope.cancel()
        }
    }
}

class Closeable(
    private val onCancel: () -> Unit,
) {
    fun cancel() {
        onCancel()
    }
}

fun <T : Any> Flow<T>.asCFlow(): CFlow<T> = CFlow(this)
