package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.service.ActionCheckService
import com.thesilentnights.easylogin.service.CommandRejectionService
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.event.CommandEvent
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.SubscribeEvent

class ActionListener {

    constructor(eventBus: IEventBus) {
        eventBus.register(this)
    }

    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (ActionCheckService.shouldCancelEvent(event.player)) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onPlayerAttack(event: LivingAttackEvent) {
        if (event.entityLiving is ServerPlayer && ActionCheckService.shouldCancelEvent(event.entityLiving)) {
            event.isCanceled = true
        }
    }


    @SubscribeEvent
    fun onPlayerDrop(event: LivingDropsEvent) {
        if (event.entity is LivingEntity && ActionCheckService.shouldCancelEvent(event.entity as LivingEntity)) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onPlayerDrop(event: CommandEvent) {
        CommandRejectionService.handleRejection(event)
    }

    fun onPlayerMove(event: ClientboundPlayerInfoPacket.PlayerUpdate) {

    }


}