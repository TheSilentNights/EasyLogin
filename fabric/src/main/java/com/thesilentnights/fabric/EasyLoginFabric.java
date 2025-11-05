package com.thesilentnights.fabric;

import com.thesilentnights.EasyLogin;
import com.thesilentnights.configs.Config;
import com.thesilentnights.configs.SqlType;
import com.thesilentnights.fabric.events.EventsRegister;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;

public final class EasyLoginFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        EasyLogin.init(Config.builder().sqlType(SqlType.SQLITE).build());
        if (Platform.getEnv() == EnvType.SERVER){
            EventsRegister.register();
        }
    }
}
