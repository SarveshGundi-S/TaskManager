package com.example.taskmanager

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class NavigationChannel {
    private val channel = Channel<NavigationEvent>(Channel.BUFFERED)

    val events = channel.receiveAsFlow()

    suspend fun send(event: NavigationEvent) {
        channel.send(event)
    }
}
