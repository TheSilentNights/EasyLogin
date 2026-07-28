package cn.thesilentnights.easylogin.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerCache {

    private static final List<UUID> loggedPlayers = new ArrayList<>();


    public static void addPlayer(UUID uuid) {
        loggedPlayers.add(uuid);
    }

    public static boolean isPlayerLogged(UUID uuid) {
        return loggedPlayers.contains(uuid);
    }

    public static void dropPlayerLogged(UUID uuid) {    
        loggedPlayers.remove(uuid);
    }
}