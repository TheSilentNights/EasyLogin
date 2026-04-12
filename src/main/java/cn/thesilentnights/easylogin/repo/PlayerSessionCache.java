package cn.thesilentnights.easylogin.repo;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.thesilentnights.easylogin.pojo.PlayerAccount;
import cn.thesilentnights.easylogin.pojo.PlayerSession;
import cn.thesilentnights.easylogin.utils.LogUtil;

public class PlayerSessionCache {

    private static final Map<String, PlayerSession> sessions = new HashMap<>();

    public static void scheduleDrop(PlayerAccount account, Long expireTimeTick) {
        LogUtil.getLogger().debug("Scheduling drop of PlayerSession {}", account.getUuid());
        LogUtil.getLogger().debug("ExpireTimeTick: {}", expireTimeTick);
        sessions.put(account.getUuid().toString(), new PlayerSession(account, System.currentTimeMillis() + expireTimeTick));
    }

    public static boolean hasSession(UUID uuid) {
        return sessions.get(uuid.toString()) != null;
    }

    public static PlayerSession getSession(UUID key) {
        PlayerSession session = sessions.get(key.toString());
        if (session == null) {
            return null;
        }
        if (session.getExpireTimeTick() < System.currentTimeMillis()) {
            sessions.remove(key.toString());
            LogUtil.getLogger().debug("PlayerSession {} expired", key);
            return null;
        }
        return session;
    }
}