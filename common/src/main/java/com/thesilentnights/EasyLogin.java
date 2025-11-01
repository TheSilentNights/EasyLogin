package com.thesilentnights;

import com.thesilentnights.sql.Mybatis;
import com.zaxxer.hikari.HikariDataSource;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;

import java.io.File;
import java.io.IOException;

public final class EasyLogin {

    public static void init() {
        //init server side database
        if (Platform.getEnvironment() == Env.SERVER){
            File file = new File(Platform.getGameFolder().toAbsolutePath()+"\\EasyLogin\\playerAccounts.db");
            if (!file.exists()){
                try{
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setJdbcUrl("jdbc:sqlite:"+file.getAbsolutePath());
            dataSource.setMaximumPoolSize(10);
            dataSource.setMinimumIdle(2);
            dataSource.setIdleTimeout(30000);
            dataSource.setAutoCommit(true);
            Mybatis.init(dataSource);
        }
    }
}
