package com.thesilentnights.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailAccountEntry {
    private String user;
    private String password;
    private String host;
    private int port;
    private String from;
    private boolean enableSSL;
    private boolean starttlsEnable;
    private long timeout;
}
