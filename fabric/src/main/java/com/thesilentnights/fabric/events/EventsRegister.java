package com.thesilentnights.fabric.events;

import com.thesilentnights.EasyLogin;
import com.thesilentnights.service.PlayerLoginService;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.world.InteractionResult;

@Slf4j
public class EventsRegister {
    static PlayerLoginService loginAuth = EasyLogin.context.getBean(PlayerLoginService.class);
    
    public static void register(){
        AttackBlockCallback.EVENT.register((playerEntity, world, hand, blockPos, direction) -> {
            log.info("block attack event");
            if (loginAuth.shouldCancelEvent(playerEntity)){
                log.info("block attack event canceled");
                return InteractionResult.FAIL;

            }
            return InteractionResult.PASS;
        });
        AttackEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (loginAuth.shouldCancelEvent(playerEntity)){
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        UseBlockCallback.EVENT.register((playerEntity, world, hand, blockHitResult) -> {
            if (loginAuth.shouldCancelEvent(playerEntity)){
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });
        UseEntityCallback.EVENT.register((playerEntity, world, hand, entity, entityHitResult) -> {
            if (loginAuth.shouldCancelEvent(playerEntity)){
                return InteractionResult.FAIL;
            }
            return InteractionResult.PASS;
        });

    }
}
