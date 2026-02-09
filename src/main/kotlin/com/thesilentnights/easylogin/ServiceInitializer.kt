package com.thesilentnights.easylogin

import cn.hutool.core.io.FileUtil
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.ActionListener
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.registrys.registerCommands
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.thesilentnights.easylogin.utils.PathAppender
import com.zaxxer.hikari.HikariDataSource
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.IEventBus


fun initializeServer(eventBus: IEventBus) {
    val dataSource = DataSource({
        val config = when (EasyLoginConfig.dataBaseType.get()) {
            DataBaseType.SQLITE -> DatasourceConfigs.generateSqliteDataSource(
                FileUtil.file(
                    PathAppender.append(
                        EasyLoginConfig.pathToDatabase.get()
                    )
                )
            )

            DataBaseType.MYSQL -> DatasourceConfigs.generateMySqlDataSource(EasyLoginConfig.pathToDatabase.get())
            else -> throw IllegalArgumentException("Invalid database type")
        }
        return@DataSource HikariDataSource(config)
    })

    DatabaseChecker(dataSource).checkDatabase()

    AccountService.init(dataSource)



    Listener()
    ActionListener(MinecraftForge.EVENT_BUS)

    eventBus.addListener { event: RegisterCommandsEvent ->
        registerCommands(event)
    }
}

fun initializeClient(eventBus: IEventBus) {
    eventBus.addListener { event: RegisterCommandsEvent ->
        registerCommands(event)
    }
}