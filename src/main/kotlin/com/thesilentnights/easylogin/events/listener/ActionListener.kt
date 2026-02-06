package com.thesilentnights.easylogin.events.listener

import com.thesilentnights.easylogin.service.ActionCheckService
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
object ActionListener {

    @JvmStatic
    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (ActionCheckService.shouldCancelEvent(event.player)) {
            event.isCanceled = true
        }
    }

    @JvmStatic
    @SubscribeEvent
    fun onPlayerAttack(event: LivingAttackEvent) {
        if (event.entityLiving is ServerPlayer && ActionCheckService.shouldCancelEvent(event.entityLiving)) {
            event.isCanceled = true
        }
    }


    @JvmStatic
    @SubscribeEvent
    fun onPlayerDrop(event: LivingDropsEvent) {
        if (event.entity is LivingEntity && ActionCheckService.shouldCancelEvent(event.entity as LivingEntity)) {
            event.isCanceled = true
        }
    }



}