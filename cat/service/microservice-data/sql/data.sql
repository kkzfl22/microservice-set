

-- 用户信息表
drop table IF EXISTS  pap_userinfo;


create table pap_userinfo
(
	OID                           	VARCHAR(60)         not null                                ,
	USER_NAME                     	VARCHAR(40)         not null                                ,
	USER_PASSWORD                 	VARCHAR(200)        not null                                ,
	REMARK                        	VARCHAR(200)                                                ,
	USER_MOBILE                   	VARCHAR(20)                                                 ,
	USER_EMAIL                    	VARCHAR(45)                                                 ,
	STATUS                        	INT(2)              not null                                ,
	CREATE_TIME                   	BIGINT(14)          not null                                ,
  primary key (OID)
);

