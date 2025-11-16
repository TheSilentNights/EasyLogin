package com.thesilentnights.sql;

import com.thesilentnights.pojo.PlayerAccount;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

public interface DatabaseProvider {
    Optional<PlayerAccount> getAuthByUUID(String uuid);
    Optional<PlayerAccount> getAuthByName(String name);
    boolean saveAuth(PlayerAccount playerAccount);
    boolean removeAuth(UUID uuid);
    Connection getConnection();
}
