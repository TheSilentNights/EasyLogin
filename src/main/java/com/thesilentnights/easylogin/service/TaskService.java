package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.service.task.Task
import java.util.*

object TaskService {
    private val taskMap: MutableMap<String, Task> = HashMap()

    fun addTask(identifier: String, task: Task) {
        taskMap[identifier] = task
    }

    fun getTask(identifier: String): Task? {
        return taskMap[identifier]
    }

    fun generateTaskIdentifier(uuid: UUID, name: String): String {
        return uuid.toString() + "_" + name
    }

    fun tick() {
        if (taskMap.isEmpty()) {
            return
        }
        taskMap.values.forEach({ task -> task.tick() })
    }

    fun cancelTask(identifier: String) {
        taskMap.remove(identifier)
    }

    fun cancelPlayer(uuid: UUID) {

        taskMap.entries.removeIf { entry: MutableMap.MutableEntry<String, Task> ->
            entry.key.startsWith(uuid.toString())
        }

    }

    enum class Suffix {
        MESSAGE, TIMEOUT
    }
}