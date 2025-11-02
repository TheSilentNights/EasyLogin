package com.thesilentnights.sql;

import com.thesilentnights.pojo.PlayerAccount;

import java.util.Optional;

public interface DatabaseProvider {
    Optional<PlayerAccount> getAuth(String username);
    boolean saveAuth(PlayerAccount playerAccount);
    boolean removeAuth(String username);
}
