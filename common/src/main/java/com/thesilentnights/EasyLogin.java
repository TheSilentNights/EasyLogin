package com.thesilentnights;

import cn.hutool.core.io.FileUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.thesilentnights.commands.EasyLoginCommands;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.events.CommonEvents;
import com.thesilentnights.events.ServerSideEvents;
import com.thesilentnights.repo.CommonStaticRepo;
import com.thesilentnights.service.EmailService;
import com.thesilentnights.service.AccountService;
import com.thesilentnights.service.TaskService;
import com.thesilentnights.service.TimerService;
import com.thesilentnights.service.task.CleanUp;
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
        if (Platform.getEnv() == EnvType.SERVER || Platform.isDevelopmentEnvironment()) {
            //init database
            initialize();
            //register events
            CommonEvents.register();
            ServerSideEvents.register();
        }

        CommandRegistrationEvent.EVENT.register((CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection) -> {
            EasyLoginCommands.register(dispatcher);
        });

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

            AccountService.init(provider);
            EmailService.init(easyLoginConfig);
        } catch (SQLException e) {
            log.warn("if you see this message.It's likely that the sql initialization has failed.", e);
        }

        CleanUp cleanUp = new CleanUp(CommonStaticRepo.cleanUpDelayTick);
        cleanUp.addCleanUpMethods((Void t) -> {
            TimerService.cleanExpired();
        });
        TaskService.addTask("cleanUp", cleanUp);
    }

}

