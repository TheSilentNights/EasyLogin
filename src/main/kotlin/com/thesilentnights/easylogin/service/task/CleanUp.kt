package com.thesilentnights.easylogin.service.task

import java.util.*
import java.util.function.Consumer


class CleanUp(var delay: Long) : Task {
    var tickCount: Long = 0
    var cleanUpMethods: LinkedList<Consumer<Void?>?> = LinkedList<Consumer<Void?>?>()

    fun addCleanUpMethods(method: Consumer<Void?>?) {
        cleanUpMethods.add(method)
    }

    override fun tick() {
        this.tickCount++
        if (this.tickCount >= this.delay) {
            this.tickCount = 0
            cleanUpMethods.forEach(Consumer { method: Consumer<Void?>? ->
                method!!.accept(null)
            })
        }
    }
}
