package com.thesilentnights.easylogin.configs

import com.thesilentnights.easylogin.utils.logInfo
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
    val enableEmailFunction: ForgeConfigSpec.BooleanValue

    val username: ForgeConfigSpec.ConfigValue<String>
    val password: ForgeConfigSpec.ConfigValue<String>
    val host: ForgeConfigSpec.ConfigValue<String>
    val port: ForgeConfigSpec.ConfigValue<Int>
    val from: ForgeConfigSpec.ConfigValue<String>
    val enableSSL: ForgeConfigSpec.ConfigValue<Boolean>
    val starttlsEnable: ForgeConfigSpec.ConfigValue<Boolean>
    val timeout: ForgeConfigSpec.ConfigValue<Long>


    init {
        logInfo(EasyLoginConfig::class, "Loading EasyLogin Config")

        val builder = ForgeConfigSpec.Builder()
        builder.comment("EasyLogin Config").push("server")
        this.dataBaseType = builder.comment("DataBase Type").defineEnum("databaseType", DataBaseType.SQLITE)
        this.pathToDatabase =
            builder.comment("Path to Database").define("pathToDatabase", "/easylogin/playerAccounts.db")
        this.enableKickOther = builder.comment("Enable kick other player when login").define("enableKickOther", false)
        this.loginTimeoutTick = builder.comment("Login timeout tick").define("loginTimeoutTick", 120 * 20L)
        this.enableEmailFunction =
            builder.comment("whether to enable email function.If you enable this,you should change the mailAccountEntry below")
                .define("enableEmailFunction", false)

        builder.comment("for email function").push("emailAccount")
        //mailAccountEntry
        username = builder.comment("").define("username", "")
        password = builder.comment("").define("password", "")
        host = builder.comment("").define("host", "")
        port = builder.comment("").define("port", 0)
        from = builder.comment("").define("from", "")
        enableSSL = builder.comment("").define("enableSSL", false)
        starttlsEnable = builder.comment("").define("starttlsEnable", false)
        timeout = builder.comment("").define("timeout", 0L)
        builder.pop()

        builder.pop()
        config = builder.build()

    }
}
