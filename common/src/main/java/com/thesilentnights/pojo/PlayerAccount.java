package com.thesilentnights.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerAccount {
    String username;
    String password;
    String lastlogin_ip;
    double lastlogin_x;
    double lastlogin_y;
    double lastlogin_z;
    String lastlogin_world;
    UUID uuid;
    String email;
    Long login_timestamp;
}
