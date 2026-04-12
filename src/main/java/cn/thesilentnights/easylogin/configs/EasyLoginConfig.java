package cn.thesilentnights.easylogin.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class EasyLoginConfig {
    public static final ForgeConfigSpec SPEC;
    public static final EasyLoginConfig INSTANCE;

    public static final ForgeConfigSpec.ConfigValue<Boolean> enableKickOther;
    public static final ForgeConfigSpec.ConfigValue<Integer> loginTimeoutTick;
    public static final ForgeConfigSpec.ConfigValue<Long> sessionExpire;
    public static final ForgeConfigSpec.ConfigValue<Boolean> enablePreLoginProtection;

    static {
        final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("EasyLogin configuration").push("general");
        
        sessionExpire = builder.define("sessionExpire", 1*60*20L);

        
        enableKickOther = builder
                .comment("Kick other players when a new player with the same username joins")
                .define("enableKickOther", true);
        loginTimeoutTick = builder
                .comment("Login timeout in ticks (20 ticks = 1 second)")
                .define("loginTimeoutTick", 1200);
        enablePreLoginProtection = builder
                .comment("Enable protection before player logs in")
                .define("enablePreLoginProtection", true);
        builder.pop();

        SPEC = builder.build();
        INSTANCE = new EasyLoginConfig();
    }


}