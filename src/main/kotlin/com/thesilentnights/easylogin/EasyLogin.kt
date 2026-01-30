package com.thesilentnights.easylogin

import com.thesilentnights.easylogin.registrys.CommandRegistrar
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod("easylogin")
object EasyLogin {
    init {
        FORGE_BUS.addListener(CommandRegistrar::onRegister)

    }
}