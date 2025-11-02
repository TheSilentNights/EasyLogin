package com.thesilentnights.sql.config;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.thesilentnights.sql.SqlLite;

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
}
