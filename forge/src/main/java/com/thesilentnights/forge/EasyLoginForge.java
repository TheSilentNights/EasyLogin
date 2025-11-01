package com.thesilentnights.forge;

import com.thesilentnights.repo.CommonStaticRepo;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.thesilentnights.EasyLogin;

@Mod(CommonStaticRepo.MOD_ID)
public final class EasyLoginForge {
    public EasyLoginForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(CommonStaticRepo.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        EasyLogin.init();
    }
}
