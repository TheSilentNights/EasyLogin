package com.thesilentnights;

import cn.hutool.core.io.FileUtil;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thesilentnights.guice.EasyLoginModule;
import com.thesilentnights.service.PlayerLoginAuth;
import com.thesilentnights.sql.config.ConfigProvider;
import dev.architectury.platform.Platform;

import java.io.File;

public final class EasyLogin {
    public static final Injector injector = Guice.createInjector(new EasyLoginModule());

    public static void init() {
        //init server side database'
        initProviders();
        injector.getInstance(PlayerLoginAuth.class).authPlayerWithPwd("re","re");
    }

    private static File getServerSideDatabasePath() {
        File file = new File(Platform.getGameFolder().toAbsolutePath() + "\\EasyLogin\\playerAccounts.db");
        return file;
    }

    private static void initProviders(){
        ConfigProvider instance = injector.getInstance(ConfigProvider.class);
        File file = getServerSideDatabasePath();
        if (!file.exists()) {
            FileUtil.copy(ClassLoader.getSystemResource("playerAccounts.db").toString(),getServerSideDatabasePath().toString(), true);
        }
        instance.setFileToDatabase(getServerSideDatabasePath());
    }
}

