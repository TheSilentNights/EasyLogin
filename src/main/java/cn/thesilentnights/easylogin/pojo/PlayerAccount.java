package cn.thesilentnights.easylogin.pojo;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public class PlayerAccount {

    private final UUID uuid;
    private String password;

    public static final Codec<PlayerAccount> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("uuid").forGetter(PlayerAccount::getUuid),
                    Codec.STRING.fieldOf("password").forGetter(PlayerAccount::getPassword)
            ).apply(instance, PlayerAccount::new)
    );

    public PlayerAccount(UUID uuid, String password) {
        this.uuid = uuid;
        this.password = password;
    }

    public UUID getUuid() {
        return uuid;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}