package com.thesilentnights.easylogin.events.listener;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.service.ActionCheckService;
import com.thesilentnights.easylogin.service.CommandRejectionService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.springframework.stereotype.Component;

@Component
public class ActionListener {

    private final CommandRejectionService commandRejectionService;


    public ActionListener(IEventBus eventBus, CommandRejectionService commandRejectionService) {
        eventBus.register(this);
        this.commandRejectionService = commandRejectionService;
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (ActionCheckService.shouldCancelEvent(event.getPlayer())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event) {
        if (event.getEntityLiving() instanceof ServerPlayer && ActionCheckService.shouldCancelEvent(event.getEntityLiving())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerDrop(LivingDropsEvent event) {
        if (event.getEntity() instanceof LivingEntity && ActionCheckService.shouldCancelEvent((LivingEntity) event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerDrop(CommandEvent event) throws CommandSyntaxException {
        commandRejectionService.handleRejection(event);
    }
}