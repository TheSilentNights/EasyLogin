package com.thesilentnights.configs;

import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.sql.SqlLite;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@ComponentScan("com.thesilentnights")
public class SpringConfig {
    @Bean
    public static EasyLoginConfig getConfig(){
        return EasyLoginConfig.readFromConfigFile();
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
