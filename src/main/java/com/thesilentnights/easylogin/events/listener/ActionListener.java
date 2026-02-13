package com.thesilentnights.easylogin.events.listener;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.repo.BlockPosRepo;
import com.thesilentnights.easylogin.service.ActionCheckService;
import com.thesilentnights.easylogin.service.CommandRejectionService;
import com.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class ActionListener {


    public ActionListener(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (ActionCheckService.shouldCancelEvent(event.getEntity())) {
            event.getEntity().sendSystemMessage(
                    TextUtil.serialize(TextUtil.FormatType.FAILURE, "you cannot interact before you log in")
            );

            event.setCanceled(true);

        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof ServerPlayer && ActionCheckService.shouldCancelEvent(event.getEntity())) {
            event.getEntity().sendSystemMessage(
                    TextUtil.serialize(TextUtil.FormatType.FAILURE, "you cannot attack before you log in")
            );
            event.setCanceled(true);
        }
    }

    //protect player
    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (EasyLoginConfig.INSTANCE.enablePreLoginProtection && event.getEntity() instanceof LivingEntity && ActionCheckService.shouldCancelEvent((LivingEntity) event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerExecuteCommand(CommandEvent event) throws CommandSyntaxException {
        CommandRejectionService.handleRejection(event);
    }


    //prevent move
    @SubscribeEvent
    public void onPlayerDrop(PlayerTickEvent.Pre event) {
        if (ActionCheckService.shouldCancelEvent(event.getEntity())) {
            BlockPos blockPos = BlockPosRepo.getBlockPos(event.getEntity().getUUID(), event.getEntity().blockPosition());
            event.getEntity().teleportTo(
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ()
            );
        }
    }
}