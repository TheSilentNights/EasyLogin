package cn.thesilentnights.easylogin.events.listener;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.repo.BlockPosRepo;
import cn.thesilentnights.easylogin.service.ActionCheckService;
import cn.thesilentnights.easylogin.service.CommandRejectionService;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.TextUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ActionListener {


    public ActionListener(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (ActionCheckService.shouldCancelEvent(event.getEntity())) {
            MessageSender.sendMessage(
                    event,
                    "you cannot interact before you log in",
                    MessageType.ERROR
            );
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer && ActionCheckService.shouldCancelEvent(event.getEntity())) {
            MessageSender.sendMessage(
                    serverPlayer,
                    "you cannot attack before you log in",
                    MessageType.ERROR
            );
            event.setCanceled(true);
        }
    }

    //protect player
    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        if (EasyLoginConfig.enablePreLoginProtection.get() && event.getEntity() instanceof LivingEntity && ActionCheckService.shouldCancelEvent((LivingEntity) event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerExecuteCommand(CommandEvent event) throws CommandSyntaxException {
        CommandRejectionService.handleRejection(event);
    }


    //prevent move
    @SubscribeEvent
    public void onPlayerDrop(TickEvent.PlayerTickEvent event) {
        if (ActionCheckService.shouldCancelEvent(event.player)) {
            BlockPos blockPos = BlockPosRepo.getBlockPos(event.player.getUUID(), event.player.blockPosition());
            event.player.teleportTo(
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ()
            );
        }
    }
}