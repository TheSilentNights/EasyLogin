package com.thesilentnights.mixin;

import com.thesilentnights.service.PlayerLoginAuth;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class PlayerMixin {

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, Vec3 pos, CallbackInfo ci) {
        if (((Object)this) instanceof ServerPlayer player){
            if (PlayerLoginAuth.shouldCancelEvent(player)){
                ci.cancel();
            }
        }
    }

}
