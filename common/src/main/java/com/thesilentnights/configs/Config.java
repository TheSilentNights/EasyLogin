package com.thesilentnights.configs;

import lombok.Data;

@Data
public class Config {
    private SqlType sqlType;
    String customPathToDatabase;


    public static Builder builder(){
        return new Builder();
    }

    //builder
    public static class Builder{
        private SqlType sqlType;
        private String customPathToDatabase;

        public Builder sqlType(SqlType sqlType){
            this.sqlType = sqlType;
            return this;
        }

        public Builder customPathToDatabase(String customPathToDatabase){
            this.customPathToDatabase = customPathToDatabase;
            return this;
        }

        public Config build(){
            Config config = new Config();
            config.sqlType = sqlType;
            config.customPathToDatabase = customPathToDatabase;
            return config;
        }
    }


}
