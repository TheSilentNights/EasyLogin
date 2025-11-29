package com.thesilentnights.sql;

import com.thesilentnights.sql.mapper.PlayerAccountMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

@Getter
public class Mybatis {
    private final SqlSessionFactory sqlSessionFactory;

    public Mybatis(HikariConfig config) {
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        Environment environment = new Environment("dev", new JdbcTransactionFactory(), hikariDataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(PlayerAccountMapper.class);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

}
