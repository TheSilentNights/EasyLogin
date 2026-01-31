package com.thesilentnights.easylogin.sql

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.resource.ResourceUtil
import com.thesilentnights.easylogin.repo.CommonStaticRepo
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File
import java.nio.file.StandardCopyOption
import kotlin.io.path.absolute
import kotlin.io.path.pathString

object DatasourceConfigs {
    private val log: Logger = LogManager.getLogger(DatasourceConfigs.javaClass)

    fun generateSqliteDataSource(fileToDataBase: java.io.File): HikariDataSource {
        log.info(fileToDataBase.toString())
        if (!fileToDataBase.exists()) {
            log.info(
                "copying file {} to {}",
                ResourceUtil.getResource("playerAccounts.db"),
                fileToDataBase.getAbsolutePath()
            )
            cn.hutool.core.io.FileUtil.copyFile(
                ResourceUtil.getResourceObj("playerAccounts.db"),
                fileToDataBase,
                StandardCopyOption.REPLACE_EXISTING
            )
        }
        return HikariDataSource(getSqliteConfig(fileToDataBase))
    }

    fun generateMySqlDataSource(url: kotlin.String?): HikariDataSource {
        return HikariDataSource(getMysqlConfig(url))
    }

    private fun getSqliteConfig(fileToDataBase: File): HikariConfig {
        val config = HikariConfig()
        config.jdbcUrl = "JDBC:sqlite:" + FileUtil.file(FMLPaths.GAMEDIR.relative().absolute().pathString,fileToDataBase.toString()).toString()
        config.isAutoCommit = false
        config.maximumPoolSize = 10
        config.minimumIdle = 2
        config.idleTimeout = 30000
        return config
    }

    private fun getMysqlConfig(url: kotlin.String?): HikariConfig {
        val config: HikariConfig = HikariConfig()
        config.jdbcUrl = "JDBC:mysql://" + url + CommonStaticRepo.TABLE_NAME
        config.maximumPoolSize = 10
        config.minimumIdle = 2
        config.idleTimeout = 30000
        return config
    }
}
