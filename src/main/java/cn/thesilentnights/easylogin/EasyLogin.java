package cn.thesilentnights.easylogin;

import cn.thesilentnights.easylogin.configs.EasyLoginConfig;
import cn.thesilentnights.easylogin.data.DataManager;
import cn.thesilentnights.easylogin.events.listener.ActionListener;
import cn.thesilentnights.easylogin.events.listener.Listener;
import cn.thesilentnights.easylogin.registrys.CommandRegistrar;
import cn.thesilentnights.easylogin.repo.CommonStaticRepo;
import cn.thesilentnights.easylogin.services.action.ActionCheckService;
import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.services.auth.PreLoginService;
import cn.thesilentnights.easylogin.services.commands.CommandRejectionService;
import cn.thesilentnights.easylogin.services.task.TaskService;
import cn.thesilentnights.easylogin.utils.Dependencies;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;

import java.nio.file.Paths;

@Mod(value = CommonStaticRepo.MOD_ID, dist = Dist.DEDICATED_SERVER)
public class EasyLogin {

        public EasyLogin(ModContainer modContainer) {

                modContainer.registerConfig(Type.SERVER, EasyLoginConfig.SPEC);

                if (FMLEnvironment.getDist() == Dist.DEDICATED_SERVER || !FMLEnvironment.isProduction()) {
                        initServer();
                }
        }

        private static void initServer() {
                DataManager.init(Paths.get(CommonStaticRepo.GAME_DIR, "easylogin").toAbsolutePath());
                new Listener(
                        Dependencies.getDependency(PreLoginService.class),
                        Dependencies.getDependency(LoginService.class),
                        Dependencies.getDependency(TaskService.class),
                        NeoForge.EVENT_BUS
                );
                new ActionListener(
                        NeoForge.EVENT_BUS,
                        Dependencies.getDependency(ActionCheckService.class),
                        Dependencies.getDependency(CommandRejectionService.class)
                );
                new CommandRegistrar(NeoForge.EVENT_BUS);

        }


}
