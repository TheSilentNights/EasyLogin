package cn.thesilentnights.easylogin.events.listener;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import cn.thesilentnights.easylogin.service.ActionCheckService;
import cn.thesilentnights.easylogin.service.CommandRejectionService;
import cn.thesilentnights.easylogin.utils.MessageSender;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract;

public class ActionListener {


    public ActionListener(IEventBus eventBus) {
        eventBus.register(this);
    }

    @SubscribeEvent
    public void onPlayerInteract(EntityInteract event) {
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
    public void onPlayerExecuteCommand(CommandEvent event) throws CommandSyntaxException {
        CommandRejectionService.handleRejection(event);
    }


    // //prevent move
    // @SubscribeEvent
    // public void onPlayerDrop(TickEvent.PlayerTickEvent event) {
    //     if (ActionCheckService.shouldCancelEvent(event.player)) {
    //         BlockPos blockPos = BlockPosRepo.getBlockPos(event.player.getUUID(), event.player.blockPosition());
    //         event.player.teleportTo(
    //                 blockPos.getX(),
    //                 blockPos.getY(),
    //                 blockPos.getZ()
    //         );
    //     }
    // }
}