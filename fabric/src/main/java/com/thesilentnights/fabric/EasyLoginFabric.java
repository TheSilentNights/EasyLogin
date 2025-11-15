package com.thesilentnights.fabric;

import com.thesilentnights.EasyLogin;
import com.thesilentnights.fabric.events.EventsRegister;
import net.fabricmc.api.ModInitializer;

public final class EasyLoginFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        EasyLogin.init();

        EventsRegister.register();


    }
}
