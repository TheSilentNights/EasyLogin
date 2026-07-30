package cn.thesilentnights.easylogin.data;

import cn.thesilentnights.easylogin.data.connections.ConnectionProvider;
import cn.thesilentnights.easylogin.data.connections.SQLiteConnectionProvider;
import cn.thesilentnights.easylogin.data.serializers.DataSerializer;
import cn.thesilentnights.easylogin.data.serializers.ExtraDataSerializer;
import cn.thesilentnights.easylogin.data.serializers.PasswordDataSerializer;
import cn.thesilentnights.easylogin.pojo.PlayerExtraData;
import cn.thesilentnights.easylogin.pojo.PlayerPasswordData;
import cn.thesilentnights.easylogin.utils.LogUtil;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataManager {

        private static ConnectionProvider passwordConnection;
        private static ConnectionProvider extraConnection;

        private static DataSerializer<PlayerPasswordData> passwordSerializer;
        private static DataSerializer<PlayerExtraData> extraSerializer;

        private static boolean initialized = false;

        public static void init(Path baseDir) {
                init(baseDir.toString());
        }

        public static synchronized void init(String baseDir) {
                if (initialized) {
                        return;
                }
                try {
                        Path dir = Paths.get(baseDir);
                        Files.createDirectories(dir);

                        passwordConnection = new SQLiteConnectionProvider();
                        passwordConnection.init(dir.resolve("passwords.db").toString());

                        extraConnection = new SQLiteConnectionProvider();
                        extraConnection.init(dir.resolve("extra_data.db").toString());

                        passwordSerializer = new PasswordDataSerializer();
                        passwordSerializer.init(passwordConnection);

                        extraSerializer = new ExtraDataSerializer();
                        extraSerializer.init(extraConnection);

                        initialized = true;
                        LogUtil.getLogger().info("DataManager initialized at {}", baseDir);
                } catch (Exception e) {
                        LogUtil.getLogger().error("Failed to initialize DataManager", e);
                        throw new RuntimeException("DataManager initialization failed", e);
                }
        }

        public static synchronized void shutdown() {
                if (!initialized) {
                        return;
                }
                try {
                        if (passwordConnection != null) {
                                passwordConnection.close();
                        }
                        if (extraConnection != null) {
                                extraConnection.close();
                        }
                } catch (Exception e) {
                        LogUtil.getLogger().error("Failed to shutdown DataManager cleanly", e);
                } finally {
                        passwordConnection = null;
                        extraConnection = null;
                        passwordSerializer = null;
                        extraSerializer = null;
                        initialized = false;
                }
        }

        public static DataSerializer<PlayerPasswordData> password() {
                ensureInitialized();
                return passwordSerializer;
        }

        public static DataSerializer<PlayerExtraData> extra() {
                ensureInitialized();
                return extraSerializer;
        }

        private static void ensureInitialized() {
                if (!initialized) {
                        throw new IllegalStateException("DataManager not initialized. Call DataManager.init() first.");
                }
        }
}
