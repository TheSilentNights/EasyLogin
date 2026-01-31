package com.thesilentnights.easylogin.configs

import net.minecraftforge.common.ForgeConfigSpec
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object EasyLoginConfig {
    val log: Logger = LogManager.getLogger(EasyLoginConfig::class.java)
    val config: ForgeConfigSpec
    val dataBaseType: ForgeConfigSpec.EnumValue<DataBaseType>
    val pathToDatabase: ForgeConfigSpec.ConfigValue<String>
    val enableKickOther: ForgeConfigSpec.BooleanValue
    val loginTimeoutTick: ForgeConfigSpec.ConfigValue<Long>

    init {
        val builder = ForgeConfigSpec.Builder()
        builder.comment("EasyLogin Config").push("server")
        this.dataBaseType = builder.comment("DataBase Type").defineEnum("databaseType", DataBaseType.SQLITE)
        this.pathToDatabase = builder.comment("Path to Database").define("pathToDatabase", "/easylogin/playerAccounts.db")
        this.enableKickOther = builder.comment("Enable kick other player when login").define("enableKickOther", false)
        this.loginTimeoutTick = builder.comment("Login timeout tick").define("loginTimeoutTick", 120*20L)
        builder.pop()
        config = builder.build()
    }
}
