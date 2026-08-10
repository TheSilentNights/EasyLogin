package cn.thesilentnights.easylogin.events.listener;

import cn.thesilentnights.easylogin.services.action.ActionCheckService;
import cn.thesilentnights.easylogin.services.commands.CommandRejectionService;
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

        private final ActionCheckService actionCheckService;
        private final CommandRejectionService commandRejectionService;



        public ActionListener(
                IEventBus eventBus,
                ActionCheckService actionCheckService,
                CommandRejectionService commandRejectionService
        ) {
                this.actionCheckService = actionCheckService;
                this.commandRejectionService = commandRejectionService;
                eventBus.register(this);
        }

        @SubscribeEvent
        public void onPlayerInteract(EntityInteract event) {
                if (actionCheckService.shouldCancelEvent(event.getEntity())) {
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
                if (actionCheckService.shouldCancelEvent(event.getEntity())) {
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
                if (actionCheckService.shouldCancelEvent(event.getEntity())) {
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
                if (actionCheckService.shouldCancelEvent(event.getEntity())) {
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
                commandRejectionService.handleRejection(event);
        }


}
