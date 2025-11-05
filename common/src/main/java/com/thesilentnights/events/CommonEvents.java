package com.thesilentnights.events;

import com.thesilentnights.repo.PlayerCache;
import com.thesilentnights.service.PlayerLoginAuth;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.text.Text;

public class CommonEvents {
    public static void register(){
        CustomEvent.LOGIN_EVENT.register((playerAccount,entity) -> {
            entity.sendMessage(Text.of(playerAccount.getUsername() + " login success"), false);
            PlayerCache.addAccount(playerAccount);
        });

        PlayerEvent.PLAYER_JOIN.register(entity -> {
            if (!PlayerLoginAuth.isLoggedIn(entity)){
                entity.sendMessage(Text.of("you haven't login or your login session has expired"),false);
            }
        });

        PlayerEvent.PLAYER_QUIT.register(entity -> {
            PlayerCache.dropAccount(entity.getEntityName());
        });

        actionEvent();
    }

    private static void actionEvent(){
        //PlayerEntity player, ItemEntity entity, ItemStack stack
        PlayerEvent.PICKUP_ITEM_PRE.register((player, entity, stack)->{
            return PlayerLoginAuth.shouldCancelEvent( player);
        });
    }
}
