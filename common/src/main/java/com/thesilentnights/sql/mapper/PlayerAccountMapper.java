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

    @Select("tables")
    void listTables();
}
