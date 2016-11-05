SET FOREIGN_KEY_CHECKS=0;

drop table if exists users;

CREATE TABLE users (
    username VARCHAR(25) NOT NULL,
    email VARCHAR(50) PRIMARY KEY NOT NULL,
    password VARCHAR(25) NOT NULL
);