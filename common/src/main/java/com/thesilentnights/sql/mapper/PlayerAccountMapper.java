package com.thesilentnights.sql.mapper;

import com.thesilentnights.pojo.PlayerAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.UUID;

@Mapper
public interface PlayerAccountMapper {
    PlayerAccount getAccountByName(String username);

    PlayerAccount getAccountByUUID(UUID uuid);

    void addAccount(PlayerAccount account);

    void updateAccount(PlayerAccount account);
}
