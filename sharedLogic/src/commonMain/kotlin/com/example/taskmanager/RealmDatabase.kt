package com.example.taskmanager

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmDatabase {
    private val configuration =
        RealmConfiguration.create(
            schema = setOf(RealmTask::class),
        )

    fun open(): Realm = Realm.open(configuration)
}
