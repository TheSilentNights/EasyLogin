package com.thesilentnights.events;

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
    }
}
