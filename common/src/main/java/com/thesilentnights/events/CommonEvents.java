package com.thesilentnights.events;

import com.thesilentnights.service.PlayerLoginService;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.PlayerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommonEvents {
    @Autowired
    private PlayerLoginService loginAuth;

    public void register(){
        actionEvent();
    }

    private void actionEvent(){
        //PlayerEntity player, ItemEntity entity, ItemStack stack
        PlayerEvent.PICKUP_ITEM_PRE.register((player, entity, stack)->{
            if (loginAuth.shouldCancelEvent(player)){
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
    }
}
