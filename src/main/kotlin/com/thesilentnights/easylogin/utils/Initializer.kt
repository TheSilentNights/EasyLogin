package com.thesilentnights.easylogin.utils

import cn.hutool.core.io.FileUtil
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.registrys.CommandRegistrar
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.service.LoginService
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.zaxxer.hikari.HikariDataSource
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initialize() {
    startKoin {
        module {
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

            single {
                DatabaseChecker(get()).checkDatabase()
                AccountService(get())
                Listener(get(),get())
                LoginService()

                CommandRegistrar(get())
            }


        }
    }



    //init service


}


