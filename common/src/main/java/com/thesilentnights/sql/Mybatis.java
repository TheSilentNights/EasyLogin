package com.thesilentnights.sql;

import com.thesilentnights.sql.config.DatabaseConfig;
import com.thesilentnights.sql.mapper.PlayerAccountMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.inject.Inject;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

public class Mybatis {
    private final SqlSessionFactory sqlSessionFactory;

    @Inject
    public Mybatis(DatabaseConfig config) {
        HikariDataSource hikariDataSource = new HikariDataSource(config.config());
        Environment environment = new Environment("dev",new JdbcTransactionFactory(),hikariDataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(PlayerAccountMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

    }

    public boolean isInitialized(){
        return sqlSessionFactory != null;
    }

    public  SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
