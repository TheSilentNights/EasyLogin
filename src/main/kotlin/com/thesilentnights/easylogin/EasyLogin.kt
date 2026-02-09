package com.thesilentnights.easylogin

import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.repo.CommonStaticRepo
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.loading.FMLLoader

@Mod(value = CommonStaticRepo.MOD_ID)
class EasyLogin {

    constructor() {
        val eventBus = MinecraftForge.EVENT_BUS
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER || !FMLLoader.isProduction()) {
            initializeServer(eventBus)
        } else {
            initializeClient(eventBus)
        }


        if (!FMLLoader.isProduction()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.config)
        } else {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EasyLoginConfig.config)
        }
    }

}