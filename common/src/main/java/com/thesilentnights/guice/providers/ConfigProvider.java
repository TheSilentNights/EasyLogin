package com.thesilentnights.guice.providers;

import cn.hutool.core.io.FileUtil;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.thesilentnights.sql.SqlLite;
import com.thesilentnights.sql.config.DatabaseConfig;
import dev.architectury.platform.Platform;

import java.io.File;

@Singleton
public class ConfigProvider implements Provider<DatabaseConfig> {
    File fileToDatabase;

    public void setFileToDatabase(File fileToDatabase) {
        this.fileToDatabase = fileToDatabase;
    }

    @Override
    public DatabaseConfig get() {
        return new DatabaseConfig(SqlLite.getSqliteConfig(fileToDatabase));
    }

    public static void init(Injector injector){
        ConfigProvider instance = injector.getInstance(ConfigProvider.class);
        File file = getServerSideDatabasePath();
        if (!file.exists()) {
            FileUtil.copy(ClassLoader.getSystemResource("playerAccounts.db").toString(),getServerSideDatabasePath().toString(), true);
        }
        instance.setFileToDatabase(getServerSideDatabasePath());
    }

    private static File getServerSideDatabasePath() {
        return new File(Platform.getGameFolder().toAbsolutePath() + "\\EasyLogin\\playerAccounts.db");
    }
}
