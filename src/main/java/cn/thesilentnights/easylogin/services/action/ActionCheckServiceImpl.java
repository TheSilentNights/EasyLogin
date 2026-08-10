package cn.thesilentnights.easylogin.services.action;

import cn.thesilentnights.easylogin.repo.PlayerCache;
import cn.thesilentnights.easylogin.services.auth.LoginService;

import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class ActionCheckServiceImpl implements ActionCheckService {

        private final LoginService loginService;

        public ActionCheckServiceImpl(LoginService loginService) {
                this.loginService = loginService;
        }

        @Override
        public boolean shouldCancelEvent(LivingEntity entity) {
                if (entity instanceof ServerPlayer) {
                        return !loginService.isPlayerLogged(entity.getUUID());
                }
                return false;
        }

        
}
