package com.thesilentnights.easylogin.service

import com.thesilentnights.easylogin.service.task.Task

object TaskService {
    private val taskMap: MutableMap<kotlin.String, Task> = HashMap<kotlin.String, Task>()

    fun addTask(identifier: kotlin.String, task: Task) {
        taskMap[identifier] = task
    }

    fun getTask(identifier: kotlin.String): Task? {
        return taskMap.get(identifier)
    }

    fun generateTaskIdentifier(uuid: java.util.UUID, taskType: TaskService.TaskType): kotlin.String {
        return uuid.toString() + "_" + taskType
    }

    fun tick() {
        if (taskMap.isEmpty()) {
            return
        }
        taskMap.values.forEach({ task -> task.tick() })
    }

    fun cancelTask(identifier: kotlin.String?) {
        taskMap.remove(identifier)
    }

    fun cancelPlayer(uuid: java.util.UUID) {
        TaskService.taskMap.entries.removeIf { entry: kotlin.collections.MutableMap.MutableEntry<kotlin.String, Task> ->
            entry.key.startsWith(
                uuid.toString()
            )
        }
    }

    enum class TaskType {
        KICK_PLAYER, MESSAGE
    }
}