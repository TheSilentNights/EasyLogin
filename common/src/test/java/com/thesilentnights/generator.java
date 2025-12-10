package com.thesilentnights;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thesilentnights.configs.DataBaseType;
import com.thesilentnights.configs.EasyLoginConfig;
import com.thesilentnights.pojo.MailAccountEntry;
import com.thesilentnights.repo.CommonStaticRepo;
import org.apache.ibatis.jdbc.SQL;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class generator {
    @Test
    public void generateJsonConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(CommonStaticRepo.ymlFactory);
        mapper.writeValue(new File("./config.yml"),new EasyLoginConfig(
                DataBaseType.SQLITE,
                CommonStaticRepo.defaultSqlitePath,
                true,
                new MailAccountEntry()
        ));
    }

    @Test
    public void generateSql(){
        SQL accounts = new SQL() {{
            INSERT_INTO("accounts");
            VALUES(
                    "username, password, lastlogin_ip, lastlogin_x, lastlogin_y, lastlogin_z, lastlogin_world, uuid, email, login_timestamp",
                    "#{username}, #{password},#{lastlogin_ip},#{lastlogin_x},#{lastlogin_y},#{lastlogin_z},#{lastlogin_world},#{uuid},#{email},#{login_timestamp}"
            );
        }};
        SQL updateAll = new SQL(){{
            UPDATE("accounts");
            SET(
                    "username=#{username}",
                    "password=#{password}",
                    "lastlogin_ip=#{lastlogin_ip}",
                    "lastlogin_world=#{lastlogin_world}",
                    "lastlogin_x=#{lastlogin_x}",
                    "lastlogin_y=#{lastlogin_y}",
                    "lastlogin_z=#{lastlogin_z}",
                    "uuid=#{uuid}",
                    "email=#{email}",
                    "login_timestamp=#{login_timestamp}"
            );
        }};
        System.out.println(updateAll);
        System.out.println(accounts);
    }
}
