package com.thesilentnights.sql;

import cn.hutool.core.io.FileUtil;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.sql.config.DatabaseConfig;
import com.thesilentnights.sql.mapper.PlayerAccountMapper;
import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;


public class SqlLite implements DatabaseProvider{
    private static final Logger log = LoggerFactory.getLogger(SqlLite.class);
    @Getter
    private static SqlLite instance;
    public static DatabaseProvider init(File fileToDataBase){
        if (instance != null){
            throw new RuntimeException(new Exception("duplicate instance creation"));
        }
        instance = new SqlLite(fileToDataBase);
        return instance;
    }

    private Mybatis mybatis;

    public SqlLite(File fileToDataBase){
        if(!fileToDataBase.exists()){
            log.info("copying file {} to {}", getClass().getClassLoader().getResource("playerAccounts.db"),fileToDataBase.getAbsolutePath());
            FileUtil.copy(Objects.requireNonNull(getClass().getClassLoader().getResource("playerAccounts.db")).getPath(),fileToDataBase.getAbsolutePath(),true);
        }
        mybatis = new Mybatis(new DatabaseConfig(getSqliteConfig(fileToDataBase)));
    }

    @Override
    public Optional<PlayerAccount> getAuth(String username) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            PlayerAccount admin = sqlSession.getMapper(PlayerAccountMapper.class).getAccountByName(username);
            return Optional.of(admin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean saveAuth(PlayerAccount playerAccount) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()){
            if (getAuth(playerAccount.getUsername()).isPresent()){
                sqlSession.getMapper(PlayerAccountMapper.class).updateAccount(playerAccount);
                sqlSession.commit();
                return true;
            }
            sqlSession.getMapper(PlayerAccountMapper.class).addAccount(playerAccount);
            sqlSession.commit();
            return true;
        }catch (Exception e){
            log.info("failed to save");
            log.error("error in insert",e);
            return false;
        }
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
        return config;
    }
}
