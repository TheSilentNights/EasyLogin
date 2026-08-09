package cn.thesilentnights.easylogin.events.listener;

import cn.thesilentnights.easylogin.services.ActionCheckService;
import cn.thesilentnights.easylogin.services.CommandRejectionService;
import cn.thesilentnights.easylogin.utils.MessageSender.MessageType;
import cn.thesilentnights.easylogin.utils.MessageSender;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

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
                        event.setCancellationResult(InteractionResult.FAIL);
                        event.setCanceled(true);
                }
        }

        @SubscribeEvent
        public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
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
        public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
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
        public void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
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


}
