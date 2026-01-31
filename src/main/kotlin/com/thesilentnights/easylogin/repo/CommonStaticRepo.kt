package com.thesilentnights.easylogin.repo

import net.minecraftforge.fml.loading.FMLPaths

object CommonStaticRepo {
    const val MOD_ID: String = "easylogin"
    val GAME_DIR: String = FMLPaths.GAMEDIR.get().toAbsolutePath().toString()
    const val TABLE_NAME: String = "accounts"
    const val configPath: String = "/easylogin/config.yml"
    const val defaultSqlitePath: String = "/easylogin/playerAccounts.db"
    const val readmePath: String = "/easylogin/readme.txt"
    const val readmeResourcePath: String = "./readme.txt"
    val cleanUpDelayTick: Long = (60 * 20).toLong()
}
