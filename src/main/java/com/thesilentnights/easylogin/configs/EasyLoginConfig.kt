package com.thesilentnights.easylogin.configs;

import com.thesilentnights.easylogin.utils.LogUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EasyLoginConfig {
    public static final Logger log = LogManager.getLogger(EasyLoginConfig.class);
    public static final ForgeConfigSpec config;

    public static final ForgeConfigSpec.EnumValue<DataBaseType> dataBaseType;
    public static final ForgeConfigSpec.ConfigValue<String> pathToDatabase;
    public static final ForgeConfigSpec.BooleanValue enableKickOther;
    public static final ForgeConfigSpec.ConfigValue<Long> loginTimeoutTick;
    public static final ForgeConfigSpec.BooleanValue enableEmailFunction;

    // Email configuration fields
    public static final ForgeConfigSpec.ConfigValue<String> username;
    public static final ForgeConfigSpec.ConfigValue<String> password;
    public static final ForgeConfigSpec.ConfigValue<String> host;
    public static final ForgeConfigSpec.ConfigValue<Integer> port;
    public static final ForgeConfigSpec.ConfigValue<String> from;
    public static final ForgeConfigSpec.ConfigValue<Boolean> enableSSL;
    public static final ForgeConfigSpec.ConfigValue<Boolean> starttlsEnable;
    public static final ForgeConfigSpec.ConfigValue<Long> timeout;

    static {
        LogUtil.logInfo(EasyLoginConfig.class, "Loading com.thesilentnights.easylogin.EasyLogin Config");
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("com.thesilentnights.easylogin.EasyLogin Config").push("server");

        dataBaseType = builder.comment("DataBase Type")
                .defineEnum("databaseType", DataBaseType.SQLITE);
        pathToDatabase = builder.comment("Path to Database")
                .define("pathToDatabase", "/easylogin/playerAccounts.db");
        enableKickOther = builder.comment("Enable kick other player when login")
                .define("enableKickOther", false);
        loginTimeoutTick = builder.comment("Login timeout tick")
                .define("loginTimeoutTick", 120 * 20L);
        enableEmailFunction = builder.comment("whether to enable email function.If you enable this,you should change the mailAccountEntry below")
                .define("enableEmailFunction", false);

        builder.comment("for email function").push("emailAccount");

        username = builder.comment("").define("username", "");
        password = builder.comment("").define("password", "");
        host = builder.comment("").define("host", "");
        port = builder.comment("").define("port", 0);
        from = builder.comment("").define("from", "");
        enableSSL = builder.comment("").define("enableSSL", false);
        starttlsEnable = builder.comment("").define("starttlsEnable", false);
        timeout = builder.comment("").define("timeout", 0L);

        builder.pop(); // pop emailAccount
        builder.pop(); // pop server

        config = builder.build();
    }
}
