package cn.thesilentnights.easylogin.utils;

import cn.thesilentnights.easylogin.services.action.ActionCheckService;
import cn.thesilentnights.easylogin.services.action.ActionCheckServiceImpl;
import cn.thesilentnights.easylogin.services.auth.LoginService;
import cn.thesilentnights.easylogin.services.auth.LoginServiceImpl;
import cn.thesilentnights.easylogin.services.auth.PreLoginService;
import cn.thesilentnights.easylogin.services.auth.PreLoginServiceImpl;
import cn.thesilentnights.easylogin.services.commands.CommandRejectionService;
import cn.thesilentnights.easylogin.services.commands.CommandRejectionServiceImpl;
import cn.thesilentnights.easylogin.services.commands.PlayerInfoService;
import cn.thesilentnights.easylogin.services.data.DataService;
import cn.thesilentnights.easylogin.services.data.DataServiceImpl;
import cn.thesilentnights.easylogin.services.passwords.ChangePasswordService;
import cn.thesilentnights.easylogin.services.passwords.ChangePasswordServiceImpl;
import cn.thesilentnights.easylogin.services.task.TaskService;
import cn.thesilentnights.easylogin.services.task.TaskServiceImpl;

import java.util.HashMap;
import java.util.Map;

public class Dependencies {
        static Map<Class<?>, Object> dependencies = new HashMap<>();

        static {
                dependencies.put(DataService.class, new DataServiceImpl());
                dependencies.put(TaskService.class, new TaskServiceImpl());

                dependencies.put(
                        LoginService.class, new LoginServiceImpl(
                                getDependency(DataService.class),
                                getDependency(TaskService.class)
                        )
                );

                dependencies.put(
                        ActionCheckService.class, new ActionCheckServiceImpl(
                                getDependency(LoginService.class)
                        )
                );

                dependencies.put(
                        CommandRejectionService.class, new CommandRejectionServiceImpl(
                                getDependency(ActionCheckService.class)
                        )
                );

                dependencies.put(
                        PreLoginService.class, new PreLoginServiceImpl(
                                getDependency(DataService.class),
                                getDependency(TaskService.class)
                        )
                );

                dependencies.put(
                        ChangePasswordService.class, new ChangePasswordServiceImpl(
                                getDependency(DataService.class),
                                getDependency(LoginService.class)
                        )
                );

                dependencies.put(
                        PlayerInfoService.class, new PlayerInfoService(
                                getDependency(DataService.class)
                        )
                );
        }

        public static <T> T getDependency(Class<T> clazz) {
                if (dependencies.containsKey(clazz)) {
                        return clazz.cast(dependencies.get(clazz));
                }
                throw new IllegalArgumentException("Dependency not registered: " + clazz);
        }

        public static void registerDependency(Class<?> clazz, Object instance) {
                dependencies.put(clazz, instance);
        }
}
