package com.thesilentnights.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

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
    String uuid;
    String email;
    Long login_timestamp;

}
