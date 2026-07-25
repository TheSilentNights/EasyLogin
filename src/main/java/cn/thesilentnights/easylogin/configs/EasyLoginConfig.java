package cn.thesilentnights.easylogin.configs;


import net.neoforged.neoforge.common.ModConfigSpec;

public class EasyLoginConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> loginTimeoutSeconds;
    public static final ModConfigSpec.ConfigValue<Boolean> enablePreLoginProtection;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("EasyLogin configuration").push("general");
        

        

        loginTimeoutSeconds = builder
                .comment("Login timeout in seconds")
                .define("loginTimeoutSeconds", 120);
        enablePreLoginProtection = builder
                .comment("Enable protection before player logs in")
                .define("enablePreLoginProtection", true);
        builder.pop();

        SPEC = builder.build();
    }


}