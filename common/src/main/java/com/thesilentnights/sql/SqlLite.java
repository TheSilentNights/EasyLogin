package com.thesilentnights.sql;

import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.sql.config.DatabaseConfig;
import com.thesilentnights.sql.mapper.PlayerAccountMapper;
import com.zaxxer.hikari.HikariConfig;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.sql.Connection;
import java.util.Optional;

public class SqlLite implements DatabaseProvider{
    private static SqlLite instance;
    public static DatabaseProvider init(File fileToDataBase){
        if (instance != null){
            throw new RuntimeException(new Exception("duplicate instance creation"));
        }
        instance = new SqlLite(fileToDataBase);
        return instance;
    }

    public static SqlLite getInstance(){
        return instance;
    }

    private Mybatis mybatis;

    public SqlLite(File fileToDataBase){
        mybatis = new Mybatis(new DatabaseConfig(getSqliteConfig(fileToDataBase)));
    }

    @Override
    public Optional<PlayerAccount> getAuth(String username) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            PlayerAccount admin = sqlSession.getMapper(PlayerAccountMapper.class).getAccountByName(username);
            return Optional.of(admin);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean saveAuth(PlayerAccount playerAccount) {
        return false;
    }

    @Override
    public boolean removeAuth(String username) {
        return false;
    }

    @Override
    public Connection getConnection() {
        return mybatis.getSqlSessionFactory().openSession().getConnection();
    }

    public static HikariConfig getSqliteConfig(File fileToDataBase){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + fileToDataBase.getAbsolutePath());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setAutoCommit(true);
        return config;
    }
}
