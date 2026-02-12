package com.thesilentnights.easylogin;

import cn.hutool.core.io.FileUtil;
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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod(value = CommonStaticRepo.MOD_ID)
public class EasyLogin {


    public EasyLogin(FMLJavaModLoadingContext context) {
        EasyLoginConfig.readFromConfigFile();
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER || !FMLLoader.isProduction()) {
            initServer();
        } else {
            initClient();
        }
    }

    private static void initServer() {
        DataSource dataSource = new DataSource(() -> {
            return DatasourceConfigs.generateSqliteDataSource(
                    FileUtil.file(
                            PathAppender.append(
                                    EasyLoginConfig.INSTANCE.pathToDatabase
                            )));
        }
        );

        new DatabaseChecker(dataSource).checkDatabase();

        AccountService.init(dataSource);

        new Listener();
        new ActionListener(MinecraftForge.EVENT_BUS);
        new CommandRegistrar(MinecraftForge.EVENT_BUS);
    }

    private static void initClient() {
        new CommandRegistrar(MinecraftForge.EVENT_BUS);
    }

}