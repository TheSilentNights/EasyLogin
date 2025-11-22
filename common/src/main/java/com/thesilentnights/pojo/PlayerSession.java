package com.thesilentnights.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerSession {
    PlayerAccount account;
    long leftTime;
}
