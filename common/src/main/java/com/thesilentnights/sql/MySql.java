package com.thesilentnights.sql;

import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.sql.mapper.PlayerAccountMapper;
import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MySql implements DatabaseProvider {
    private final Mybatis mybatis;

    public MySql(String url) {
        mybatis = new Mybatis(getMysqlConfig(url));
    }

    @Override
    public Optional<PlayerAccount> getAuthByUUID(UUID uuid) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            return Optional.ofNullable(sqlSession.getMapper(PlayerAccountMapper.class).getAccountByUUID(uuid));
        } catch (Exception e) {
            log.error("error while doing select", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<PlayerAccount> getAuthByName(String name) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            return Optional.ofNullable(sqlSession.getMapper(PlayerAccountMapper.class).getAccountByName(name));
        } catch (Exception e) {
            log.error("error while doing select", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean saveAuth(PlayerAccount playerAccount) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            if (getAuthByUUID(playerAccount.getUuid()).isPresent()) {
                log.info(playerAccount.toString());
                sqlSession.getMapper(PlayerAccountMapper.class).updateAccount(playerAccount);
                sqlSession.commit();
                return true;
            }
            sqlSession.getMapper(PlayerAccountMapper.class).addAccount(playerAccount);
            sqlSession.commit();
            return true;
        } catch (Exception e) {
            log.error("failed to save");
            log.error("error in insert", e);
            return false;
        }
    }

    @Override
    public boolean removeAuth(UUID uuid) {
        return false;
    }

    @Override
    public Connection getConnection() {
        return null;
    }

    private static HikariConfig getMysqlConfig(String url) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        return config;
    }
}
