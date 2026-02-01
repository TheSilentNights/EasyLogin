package com.thesilentnights.easylogin

import com.thesilentnights.easylogin.events.listener.ActionListener
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.utils.Initializer
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod(value = "easylogin")
object EasyLogin {
    val log: Logger = LogManager.getLogger(EasyLogin::class.java)

    init {
        Initializer.init()
        FORGE_BUS.addListener(CommandRegistrar::onRegister)

        Listener()
        ActionListener()

    }

}