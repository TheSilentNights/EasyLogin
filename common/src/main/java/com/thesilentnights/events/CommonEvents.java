package com.thesilentnights.events;

import com.thesilentnights.events.ievents.PlayerLoginEvent;
import com.thesilentnights.repo.BlockPosRepo;
import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.service.PlayerLoginAuth;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;

public class CommonEvents {
    public static void register(){
        actionEvent();
    }

    private static void actionEvent(){
        //PlayerEntity player, ItemEntity entity, ItemStack stack
        PlayerEvent.PICKUP_ITEM_PRE.register((player, entity, stack)->{
            if (PlayerLoginAuth.shouldCancelEvent(player)){
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });

        PlayerLoginEvent.ON_LOGIN.register(((account, serverPlayer) -> {
            BlockPosRepo.removeBlockPos(account.getUsername());
            PlayerCache.addAccount(account);
        }));
    }
}
