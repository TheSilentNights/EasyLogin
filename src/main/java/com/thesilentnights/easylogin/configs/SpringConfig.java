package com.thesilentnights.easylogin.configs;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.easylogin.sql.DataSource;
import com.thesilentnights.easylogin.sql.DatasourceConfigs;
import com.thesilentnights.easylogin.utils.PathAppender;
import com.zaxxer.hikari.HikariConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public DataSource dataSource() {
        return new DataSource(() -> {
            switch (EasyLoginConfig.dataBaseType.get()){
                case SQLITE -> {
                    return DatasourceConfigs.generateSqliteDataSource(
                            FileUtil.file(
                                    PathAppender.append(
                                            EasyLoginConfig.pathToDatabase.get()
                                    )));
                }

                case MYSQL ->{
                    return DatasourceConfigs.generateMySqlDataSource(EasyLoginConfig.pathToDatabase.get());
                }

                default -> throw new IllegalArgumentException("Invalid database type")
            }

        });
    }
}

