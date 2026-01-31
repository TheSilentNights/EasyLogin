package com.thesilentnights.easylogin

import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.utils.Initializer
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(value = "easylogin")
object EasyLogin {
    val log: Logger = LogManager.getLogger(EasyLogin::class.java)
    init{
        Initializer.init()
        MinecraftForge.EVENT_BUS.addListener(CommandRegistrar::onRegister)

    }

}