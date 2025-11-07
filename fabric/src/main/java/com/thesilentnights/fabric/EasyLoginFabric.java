package com.thesilentnights.fabric;

import com.thesilentnights.EasyLogin;
import com.thesilentnights.configs.Config;
import com.thesilentnights.fabric.configs.FabricConfig;
import com.thesilentnights.fabric.events.EventsRegister;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public final class EasyLoginFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        AutoConfig.register(FabricConfig.class, GsonConfigSerializer::new);

        // Run our common setup.
        FabricConfig config = AutoConfig.getConfigHolder(FabricConfig.class).getConfig();
        EasyLogin.init(
                Config.builder()
                        .sqlType(config.getSqlType())
                        .customPathToDatabase(config.getCustomPathToDatabase())
                        .build()
        );

        EventsRegister.register();


    }
}
