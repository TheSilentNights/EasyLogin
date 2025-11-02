package com.thesilentnights.guice;

import com.google.inject.AbstractModule;
import com.thesilentnights.sql.config.ConfigProvider;
import com.thesilentnights.sql.config.DatabaseConfig;

public class EasyLoginModule extends AbstractModule {
    @Override
    protected void configure() {
        //bind DatabaseConfig provider
        bind(DatabaseConfig.class).toProvider(ConfigProvider.class);
    }
}
