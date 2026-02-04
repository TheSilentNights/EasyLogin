package com.thesilentnights.easylogin

import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.ActionListener
import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.utils.initialize
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.loading.FMLLoader
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Mod(value = "easylogin")
class EasyLogin: KoinComponent{

    constructor() {
        initialize()

        MinecraftForge.EVENT_BUS.register(get<CommandRegistrar>()::onRegister)


        ActionListener()

        if (!FMLLoader.isProduction()){
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.config)
        }else{
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EasyLoginConfig.config)
        }
    }

}