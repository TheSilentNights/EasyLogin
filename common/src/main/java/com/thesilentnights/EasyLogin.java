package com.thesilentnights;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.thesilentnights.guice.EasyLoginModule;
import com.thesilentnights.guice.providers.ConfigProvider;
import com.thesilentnights.service.PlayerLoginAuth;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;

public final class EasyLogin {
    public static final Injector injector = Guice.createInjector(new EasyLoginModule());

    public static void init() {
        //init server side database'
        if (Platform.getEnv() == EnvType.SERVER || Platform.isDevelopmentEnvironment()) {
            //init providers
            initProviders();

            // test
            if (Platform.isDevelopmentEnvironment()) {
                test();
            }
        }
    }

    private static void initProviders() {
        ConfigProvider.init(EasyLogin.injector);
    }

    private static void test() {
        //test
        injector.getInstance(PlayerLoginAuth.class).authPlayerWithPwd("re", "re");
    }
}

