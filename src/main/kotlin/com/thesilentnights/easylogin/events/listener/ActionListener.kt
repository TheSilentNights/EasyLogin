package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.service.ActionCheckService
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent

class ActionListener {
    init {
        MinecraftForge.EVENT_BUS.addListener(::onPlayerInteract)
        MinecraftForge.EVENT_BUS.addListener(::onPlayerAttack)
        MinecraftForge.EVENT_BUS.addListener(::onPlayerDrop)

    }

    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (ActionCheckService.shouldCancelEvent(event.player)) {
            event.isCanceled = true
        }
    }

    fun onPlayerAttack(event: LivingAttackEvent) {
        if (event.entityLiving is ServerPlayer && ActionCheckService.shouldCancelEvent(event.entityLiving)) {
            event.isCanceled = true
        }
    }


    fun onPlayerDrop(event: LivingDropsEvent) {
        if (event.entity is LivingEntity && ActionCheckService.shouldCancelEvent(event.entity as LivingEntity)) {
            event.isCanceled = true
        }
    }

}