package com.thesilentnights.pojo;

import lombok.Data;

@Data
public class PlayerAccount {
    String username;
    String password;
    String lastlogin_ip;
    double lastlogin_x;
    double lastlogin_y;
    double lastlogin_z;
    String lastlogin_world;
    String uuid;
    String email;
    Long login_timestamp;

    public PlayerAccount(String name, String password, String lastlogin_ip, double lastlogin_x, double lastlogin_y, double lastlogin_z, String lastlogin_world, String uuid, String email, Long login_timestamp) {
        this.username = name;
        this.password = password;
        this.lastlogin_ip = lastlogin_ip;
        this.lastlogin_x = lastlogin_x;
        this.lastlogin_y = lastlogin_y;
        this.lastlogin_z = lastlogin_z;
        this.lastlogin_world = lastlogin_world;
        this.uuid = uuid;
        this.email = email;
        this.login_timestamp = login_timestamp;
    }
}
