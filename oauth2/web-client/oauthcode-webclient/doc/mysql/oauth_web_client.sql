CREATE DATABASE if not exists microservice DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
CREATE USER 'testuser'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON microservice.* TO 'testuser'@'%';

use microservice;

create table auth2_webclient_user(
    id bigint auto_increment primary key,
    username varchar(100),
    password varchar(50),
    access_token varchar(100) NULL,
    access_token_validity datetime NULL,
    refresh_token varchar(100) NULL
);

insert into auth2_webclient_user
(username, password)
value
('liujun', 'feifei');