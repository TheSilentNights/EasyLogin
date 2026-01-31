package com.thesilentnights.easylogin.utils

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.resource.ResourceUtil
import com.thesilentnights.easylogin.configs.DataBaseType
import com.thesilentnights.easylogin.configs.EasyLoginConfig
import com.thesilentnights.easylogin.events.listener.Listener
import com.thesilentnights.easylogin.service.AccountService
import com.thesilentnights.easylogin.sql.DataSource
import com.thesilentnights.easylogin.sql.DatabaseChecker
import com.thesilentnights.easylogin.sql.DatasourceConfigs
import com.zaxxer.hikari.HikariDataSource
import net.minecraftforge.fml.loading.FMLPaths

object Initializer {
    fun init(){
        //prepare sqlite database
        if (EasyLoginConfig.dataBaseType.get() == DataBaseType.SQLITE){
            checkAndGenerateSqliteDb()
        }
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

    private fun checkAndGenerateSqliteDb(){
        val file = FileUtil.file(FMLPaths.GAMEDIR.toString(),EasyLoginConfig.pathToDatabase.get())
        if (!file.exists()){
            FileUtil.copyFile(ResourceUtil.getResourceObj("playerAccounts.db"),file)
        }
    }
}