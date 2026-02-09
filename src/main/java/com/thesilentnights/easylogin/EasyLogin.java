package com.thesilentnights.easylogin;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.easylogin.commands.EasyLoginCommands;
import com.thesilentnights.easylogin.configs.EasyLoginConfig;
import com.thesilentnights.easylogin.events.listener.ActionListener;
import com.thesilentnights.easylogin.events.listener.Listener;
import com.thesilentnights.easylogin.registrys.CommandRegistrar;
import com.thesilentnights.easylogin.repo.CommonStaticRepo;
import com.thesilentnights.easylogin.service.AccountService;
import com.thesilentnights.easylogin.sql.DataSource;
import com.thesilentnights.easylogin.sql.DatabaseChecker;
import com.thesilentnights.easylogin.sql.DatasourceConfigs;
import com.thesilentnights.easylogin.utils.PathAppender;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(value = CommonStaticRepo.MOD_ID)
public class EasyLogin {

    public EasyLogin() {
        if (!FMLLoader.isProduction()) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EasyLoginConfig.config);
        } else {
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, EasyLoginConfig.config);
        }

        if (FMLLoader.isProduction() && FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
            initServer();
        } else {
            initClient();
        }


    }

    private static void initServer() {
        DataSource dataSource = new DataSource(() -> {
            switch (EasyLoginConfig.dataBaseType.get()) {
                case SQLITE -> {
                    return DatasourceConfigs.generateSqliteDataSource(
                            FileUtil.file(
                                    PathAppender.append(
                                            EasyLoginConfig.pathToDatabase.get()
                                    )));
                }

                case MYSQL -> {
                    return DatasourceConfigs.generateMySqlDataSource(EasyLoginConfig.pathToDatabase.get());
                }

                default -> throw new IllegalArgumentException("Invalid database type");
            }

        });

        new DatabaseChecker(dataSource).checkDatabase();

        AccountService.init(dataSource);

        new Listener();
        new ActionListener(MinecraftForge.EVENT_BUS);
        new CommandRegistrar(new EasyLoginCommands(), MinecraftForge.EVENT_BUS);
    }

    private static void initClient() {
        new CommandRegistrar(new EasyLoginCommands(), MinecraftForge.EVENT_BUS);
    }

}