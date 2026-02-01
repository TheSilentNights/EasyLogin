package com.thesilentnights.easylogin.utils

import cn.hutool.core.io.FileUtil
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.zaxxer.hikari.HikariDataSource

object Initializer {
    fun init() {
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

        //init service
        AccountService.init(dataSource)


    }


}