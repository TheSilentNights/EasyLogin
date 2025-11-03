package com.thesilentnights.guice;

import com.google.inject.AbstractModule;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.service.PlayerLoginAuthImpl;
import com.thesilentnights.guice.providers.ConfigProvider;
import com.thesilentnights.sql.config.DatabaseConfig;

public class EasyLoginModule extends AbstractModule {
    @Override
    protected void configure() {
        //bind DatabaseConfig provider
        bind(DatabaseConfig.class).toProvider(ConfigProvider.class);

        //bind impl
        bind(PlayerLoginAuth.class).to(PlayerLoginAuthImpl.class);
    }
}
