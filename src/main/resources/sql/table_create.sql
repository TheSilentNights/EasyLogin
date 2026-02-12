CREATE TABLE accounts
(
    uuid            TEXT PRIMARY KEY
        UNIQUE
                         NOT NULL,
    password        TEXT NOT NULL,
    lastlogin_x     NUMERIC,
    lastlogin_y     NUMERIC,
    lastlogin_z     NUMERIC,
    lastlogin_ip    TEXT,
    lastlogin_world TEXT,
    username        TEXT,
    login_timestamp TIMESTAMP
);
