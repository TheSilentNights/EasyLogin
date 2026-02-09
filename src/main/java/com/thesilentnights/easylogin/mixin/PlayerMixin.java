package com.thesilentnights.easylogin.mixin;


import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class PlayerMixin {


    @Shadow
    @Final
    private MinecraftServer server;

    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    public void tick(ServerboundMovePlayerPacket pPacket, CallbackInfo ci) {
        LogManager.getLogger().info("tick");

    }


}
