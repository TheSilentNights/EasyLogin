package com.thesilentnights.easylogin

import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.ActionListener
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.utils.initialize
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.loading.FMLLoader
import org.koin.core.context.startKoin
import org.koin.dsl.module

@Mod(value = "easylogin")
class EasyLogin{

    constructor() {
        initialize()
        MinecraftForge.EVENT_BUS.addListener(CommandRegistrar::onRegister)

        startKoin {
            module {

            }
        }

        Listener()
        ActionListener()

        if (!FMLLoader.isProduction()){
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.config)
        }else{
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EasyLoginConfig.config)
        }
    }

}