package cn.thesilentnights.easylogin.repo;

import java.util.*;

public class PlayerCache {

        private static final Set<UUID> loggedPlayers = new HashSet<>();


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
