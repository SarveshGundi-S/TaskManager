package com.example.taskmanager

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class RealmTask : RealmObject {
    @PrimaryKey
    var id: Int = 0

    var title: String = ""
    var description: String = ""
    var isCompleted: Boolean = false
}
