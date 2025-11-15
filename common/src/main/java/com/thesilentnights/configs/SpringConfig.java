package com.thesilentnights.configs;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.sql.SqlLite;
import dev.architectury.platform.Platform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ComponentScan("com.thesilentnights")
public class SpringConfig {
    @Bean
    public static EasyLoginConfig getConfig(){
        File file = FileUtil.file(Platform.getGameFolder().toFile(), "playerAccounts.db");
        return new EasyLoginConfig(DataBaseType.SQLITE,file.getAbsolutePath());
    }

    @Bean
    public static DatabaseProvider getProvider(EasyLoginConfig config){
        switch (config.dataBaseType){
            case SQLITE -> {
                return new SqlLite(new File(config.getPathToDatabase()));
            }case MYSQL -> {
                return null;
            }default -> {
                return null;
            }
        }
    }
}
