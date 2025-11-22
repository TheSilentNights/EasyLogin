package com.thesilentnights.sql.mapper;

import com.thesilentnights.pojo.PlayerAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PlayerAccountMapper {
    @Select("SELECT * " +
            "FROM accounts " +
            "WHERE username='${username}'")
    PlayerAccount getAccountByName(String username);

    @Select("SELECT * " +
            "FROM accounts " +
            "WHERE uuid='${uuid}'")
    PlayerAccount getAccountByUUID(String uuid);

    @Select("tables")
    void listTables();

    @Select("INSERT INTO accounts " +
            "(username, password, lastlogin_ip, lastlogin_x, lastlogin_y, lastlogin_z, lastlogin_world, uuid, email, login_timestamp) " +
            "VALUES " +
            "('${username}', '${password}', '${lastlogin_ip}', '${lastlogin_x}', '${lastlogin_y}', '${lastlogin_z}', '${lastlogin_world}', '${uuid}', '${email}', '${login_timestamp}')")
    void addAccount(PlayerAccount account);

    @Select("UPDATE accounts " +
            "SET " +
            "password='${password}', " +
            "lastlogin_ip='${lastlogin_ip}', " +
            "lastlogin_x='${lastlogin_x}', " +
            "lastlogin_y='${lastlogin_y}', " +
            "lastlogin_z='${lastlogin_z}', " +
            "lastlogin_world='${lastlogin_world}', " +
            "username='${username}', " +
            "email='${email}', " +
            "login_timestamp='${login_timestamp}'" +
            "WHERE uuid='${uuid}'")
    void updateAccount(PlayerAccount account);
}
