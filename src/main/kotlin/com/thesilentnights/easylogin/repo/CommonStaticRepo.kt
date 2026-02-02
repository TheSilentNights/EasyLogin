package com.thesilentnights.easylogin.repo

import com.thesilentnights.easylogin.EasyLogin
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object CommonStaticRepo {
    const val MOD_ID: String = "easylogin"
    val GAME_DIR: String = FMLPaths.GAMEDIR.get().toAbsolutePath().toString()
    const val TABLE_NAME: String = "accounts"
    val log: Logger = LogManager.getLogger(EasyLogin::class.java)
}
