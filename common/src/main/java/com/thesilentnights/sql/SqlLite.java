package com.thesilentnights.sql;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.thesilentnights.pojo.PlayerAccount;
import com.thesilentnights.sql.config.DatabaseConfig;
import com.thesilentnights.sql.mapper.PlayerAccountMapper;
import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SqlLite implements DatabaseProvider{

    private final Mybatis mybatis;

    public SqlLite(File fileToDataBase){
        if(!fileToDataBase.exists()){
            log.info("copying file {} to {}", ResourceUtil.getResource("playerAccounts.db"),fileToDataBase.getAbsolutePath());
            FileUtil.copyFile(ResourceUtil.getResourceObj("playerAccounts.db"),fileToDataBase, StandardCopyOption.REPLACE_EXISTING);
        }
        mybatis = new Mybatis(new DatabaseConfig(getSqliteConfig(fileToDataBase)));
    }

    @Override
    public Optional<PlayerAccount> getAuthByName(String username) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            PlayerAccount admin = sqlSession.getMapper(PlayerAccountMapper.class).getAccountByName(username);
            return Optional.of(admin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<PlayerAccount> getAuthByUUID(String uuid) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()) {
            PlayerAccount admin = sqlSession.getMapper(PlayerAccountMapper.class).getAccountByUUID(uuid);
            return Optional.of(admin);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


    @Override
    public boolean saveAuth(PlayerAccount playerAccount) {
        try (SqlSession sqlSession = mybatis.getSqlSessionFactory().openSession()){
            if (getAuthByUUID(playerAccount.getUuid()).isPresent()){
                log.info(playerAccount.toString());
                sqlSession.getMapper(PlayerAccountMapper.class).updateAccount(playerAccount);
                sqlSession.commit();
                return true;
            }
            sqlSession.getMapper(PlayerAccountMapper.class).addAccount(playerAccount);
            sqlSession.commit();
            return true;
        }catch (Exception e){
            log.error("failed to save");
            log.error("error in insert",e);
            return false;
        }
    }

    @Override
    public boolean removeAuth(UUID uuid) {
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
