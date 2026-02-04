package com.thesilentnights.easylogin.utils

import cn.hutool.core.io.FileUtil
import com.thesilentnights.easylogin.commands.EasyLoginCommands
import com.thesilentnights.easylogin.commands.admin.ByPass
import com.thesilentnights.easylogin.commands.admin.EmailTest
import com.thesilentnights.easylogin.commands.admin.PlayerInfoCommands
import com.thesilentnights.easylogin.commands.admin.TeleportToOfflinePlayer
import com.thesilentnights.easylogin.commands.common.*
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.service.ChangePasswordService
import com.thesilentnights.easylogin.service.LoginService
import com.thesilentnights.easylogin.service.PasswordRecoveryService
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.zaxxer.hikari.HikariDataSource
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

fun initialize() {
    GlobalContext.startKoin {
        modules(
            module(createdAtStart = true) {
                //data
                single {
                    DataSource({
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
                }
                single { DatabaseChecker(get()).checkDatabase() }

                //service
                single { AccountService(get()) }
                single { ChangePasswordService(get()) }
                single { PasswordRecoveryService(get())}
                single { LoginService() }


                //common commands
                single { LoginCommands(get()) }
                single { RegistrarCommands(get()) }
                single { LogoutCommand() }
                single { RecoverCommands(get()) }
                single { ChangePassword(get()) }
                single { Email() }


                //admin commands
                single { ByPass() }
                single { EmailTest() }
                single { PlayerInfoCommands() }
                single { TeleportToOfflinePlayer() }
                single { EasyLoginCommands() }

                //registrar
                single { CommandRegistrar(get()) }
                single { Listener(get<AccountService>(), get<LoginService>()) }
            })
    }
}