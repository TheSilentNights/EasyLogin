package com.thesilentnights.easylogin

import cn.hutool.core.io.FileUtil
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.zaxxer.hikari.HikariDataSource
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(value = "easylogin")
object EasyLogin {
    val log: Logger = LogManager.getLogger(EasyLogin::class.java)
    init{
        MinecraftForge.EVENT_BUS.addListener(CommandRegistrar::onRegister)
        val dataSource = DataSource({
            val config = when (EasyLoginConfig.dataBaseType.get()) {
                DataBaseType.SQLITE -> DatasourceConfigs.generateSqliteDataSource(FileUtil.file(EasyLoginConfig.pathToDatabase.get()))
                DataBaseType.MYSQL -> DatasourceConfigs.generateMySqlDataSource(EasyLoginConfig.pathToDatabase.get())
                else -> throw IllegalArgumentException("Invalid database type")
            }
            return@DataSource HikariDataSource(config)
        })

        DatabaseChecker(dataSource).checkDatabase()

        //init service
        AccountService.init(dataSource)

        Listener()
    }

}