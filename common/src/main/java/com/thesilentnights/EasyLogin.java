package com.thesilentnights;

import cn.hutool.core.io.FileUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.commands.EasyLoginCommands;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.events.CommonEvents;
import com.thesilentnights.events.ServerSideEvents;
import com.thesilentnights.service.PlayerLoginService;
import com.thesilentnights.sql.DatabaseChecker;
import com.thesilentnights.sql.DatabaseProvider;
import com.thesilentnights.sql.MySql;
import com.thesilentnights.sql.SqlLite;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.platform.Platform;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.EnvType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import java.sql.SQLException;

@Slf4j
public final class EasyLogin {

    public static void init() {
        //init server side database'
        if (Platform.getEnv() == EnvType.SERVER) {
            //init database
            initialize();
            //register events
            CommonEvents.register();
            ServerSideEvents.register();

            // test
            log.info("test");
            if (Platform.isDevelopmentEnvironment()) {
                test();
            }
        }

        CommandRegistrationEvent.EVENT.register((CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) -> {
            EasyLoginCommands.register(dispatcher);
        });

        if (Platform.isDevelopmentEnvironment()) {
            devServerSideLoader();
        }
    }

    /***
     * just for loading server side environment for development
     * ignore it
     */
    public static void devServerSideLoader() {

        try {
            initialize();
            log.info("debug init events");
            //register events
            CommonEvents.register();
            ServerSideEvents.register();

            // test
            log.info("debug test");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static void initialize() {
        EasyLoginConfig easyLoginConfig = EasyLoginConfig.readFromConfigFile();
        DatabaseProvider provider = null;
        try {
            switch (easyLoginConfig.getDataBaseType()) {
                case SQLITE -> {
                    provider = new SqlLite(FileUtil.file(Platform.getGameFolder().toString(), easyLoginConfig.getPathToDatabase()));
                }
                case MYSQL -> {
                    provider = new MySql(easyLoginConfig.getPathToDatabase());
                }
            }
            new DatabaseChecker(provider).checkAndRepairTable();

            PlayerLoginService.init(provider);
        } catch (SQLException e) {
            log.warn("if you see this message.It's likely that the sql initialization has failed.", e);
        }
    }


    private static void test() {
        //test
    }
}

