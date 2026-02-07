package com.thesilentnights.easylogin.sql;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.StandardCopyOption;

public class DatasourceConfigs {
    private static final Logger log = LogManager.getLogger(DatasourceConfigs.class);

    public static HikariDataSource generateSqliteDataSource(File fileToDataBase) {
        log.info(fileToDataBase.toString());
        if (!fileToDataBase.exists()) {
            log.info("copying file {} to {}", ResourceUtil.getResource("playerAccounts.db"), fileToDataBase.getAbsolutePath());
            FileUtil.copyFile(
                    ResourceUtil.getResourceObj("playerAccounts.db"),
                    fileToDataBase,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
        return new HikariDataSource(getSqliteConfig(fileToDataBase));
    }

    public static HikariDataSource generateMySqlDataSource(String url) {
        return new HikariDataSource(getMysqlConfig(url));
    }

    private static HikariConfig getSqliteConfig(File fileToDataBase) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("JDBC:sqlite:" + fileToDataBase.getAbsolutePath());
        config.setAutoCommit(false);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        return config;
    }

    private static HikariConfig getMysqlConfig(String url) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("JDBC:mysql://" + url + CommonStaticRepo.TABLE_NAME);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        return config;
    }
}