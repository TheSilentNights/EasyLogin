package com.thesilentnights.sql;

import com.thesilentnights.pojo.PlayerAccount;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Optional;

public class SqlLite implements DatabaseProvider{
    SqlSessionFactory mybatis = Mybatis.getSqlSessionFactory();

    @Override
    public Optional<PlayerAccount> getAuth() {
        return Optional.empty();
    }

    @Override
    public boolean saveAuth(PlayerAccount playerAccount) {
        return false;
    }

    @Override
    public boolean removeAuth(String username) {
        return false;
    }
}
