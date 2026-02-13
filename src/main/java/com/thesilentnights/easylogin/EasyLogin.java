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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;

import java.util.Objects;

@Mod(value = CommonStaticRepo.MOD_ID)
public class EasyLogin {


    public EasyLogin(FMLModContainer context) {
        EasyLoginConfig.readFromConfigFile();
        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER || !FMLLoader.isProduction()) {
            initServer(context);
        } else {
            initClient(context);
        }
    }

    private static void initServer(FMLModContainer container) {
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

        new Listener(Objects.requireNonNull(NeoForge.EVENT_BUS));
        new ActionListener(Objects.requireNonNull(NeoForge.EVENT_BUS));
        new CommandRegistrar(NeoForge.EVENT_BUS);
    }

    private static void initClient(ModContainer container) {
        new CommandRegistrar(Objects.requireNonNull(container.getEventBus()));
    }

}