package com.thesilentnights.configs;

public class Config {
    private SqlType sqlType;

    public SqlType getSqlType() {
        return sqlType;
    }


    public static Builder builder(){
        return new Builder();
    }

    //builder
    public static class Builder{
        private SqlType sqlType;

        public Builder sqlType(SqlType sqlType){
            this.sqlType = sqlType;
            return this;
        }

        public Config build(){
            Config config = new Config();
            config.sqlType = sqlType;
            return config;
        }
    }


}
