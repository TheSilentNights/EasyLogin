package com.thesilentnights.sql.config;

import com.zaxxer.hikari.HikariConfig;

public record DatabaseConfig(HikariConfig config) {

}
