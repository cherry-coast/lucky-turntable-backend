drop table if exists lucky_user;
CREATE TABLE `lucky_user`
(
    `ID`          BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `NAME`        VARCHAR(100) NOT NULL DEFAULT '' COMMENT '微信昵称',
    `OPEN_ID`     VARCHAR(100) NOT NULL DEFAULT '' COMMENT '微信标识',
    `GENDER`      INT(2)                DEFAULT NULL COMMENT '性别',
    `CITY`        VARCHAR(100) NOT NULL DEFAULT '' COMMENT '国家',
    `PROVINCE`    VARCHAR(100) NOT NULL DEFAULT '' COMMENT '省',
    `COUNTRY`     VARCHAR(100) NOT NULL DEFAULT '' COMMENT '区',
    `INSERT_DATA` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `UPDATE_DATA` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `DEL`         BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '是否删除,0:否,1:是,默认否',
    PRIMARY KEY (`ID`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


drop table if exists lucky_prize;
CREATE TABLE `lucky_prize`
(
    `ID`                BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `OPEN_ID`           VARCHAR(100) NOT NULL DEFAULT '' COMMENT '微信唯一标识',
    `PRIZE_NAME`        VARCHAR(100) NOT NULL DEFAULT '' COMMENT '奖品名称',
    `PRIZE_IMAGE`       VARCHAR(500) NOT NULL DEFAULT '' COMMENT '奖品图片',
    `PRIZE_PROBABILITY` DOUBLE                DEFAULT 0.00 COMMENT '奖品概率',
    `PRIZE_NUM`         INT(5)       NOT NULL DEFAULT 0 COMMENT '奖品数量',
    `PRIZE_UNIT`        VARCHAR(10)  NOT NULL DEFAULT '' COMMENT '奖品数量单位',
    `INSERT_DATA`       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `UPDATE_DATA`       TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `DEL`               BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '是否删除,0:否,1:是,默认否',
    PRIMARY KEY (`ID`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


drop table if exists lucky_win_history;
CREATE TABLE `lucky_win_history`
(
    `ID`           BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `OPEN_ID`      VARCHAR(100) NOT NULL DEFAULT '' COMMENT '微信唯一标识',
    `PRIZE_NAME`   VARCHAR(100) NOT NULL DEFAULT '' COMMENT '奖品名称',
    `WINNING_DATE` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '中奖时间',
    `INSERT_DATA`  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `UPDATE_DATA`  TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `DEL`          BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '是否删除,0:否,1:是,默认否',
    PRIMARY KEY (`ID`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

drop table if exists lucky_interface_log;
CREATE TABLE lucky_interface_log
(
    `ID`          INT     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `DESCRIPTION` VARCHAR(255)     DEFAULT '' COMMENT '操作描述',
    `USERNAME`    VARCHAR(255)     DEFAULT '' COMMENT '操作用户',
    `SPEND_TIME`  BIGINT           DEFAULT 0 COMMENT '消耗时间',
    `BASE_PATH`   VARCHAR(255)     DEFAULT '' COMMENT '根路径',
    `URI`         VARCHAR(255)     DEFAULT '' COMMENT 'URI',
    `URL`         VARCHAR(255)     DEFAULT '' COMMENT 'URL',
    `METHOD`      VARCHAR(255)     DEFAULT '' COMMENT '请求类型',
    `IP`          VARCHAR(255)     DEFAULT '' COMMENT 'IP地址',
    `PARAMETER`   TEXT COMMENT '请求参数',
    `RESULT`      TEXT COMMENT '返回结果',
    `RECORD_TIME` BIGINT           DEFAULT 0 COMMENT '开始时间',
    `INSERT_DATA` TIMESTAMP        DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `UPDATE_DATA` TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `DEL`         BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否删除,0:否,1:是,默认否',
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='接口日志表';


drop table if exists lucky_back_user;
CREATE TABLE `lucky_back_user`
(
    `ID`              BIGINT(11)   NOT NULL AUTO_INCREMENT,
    `NAME`            VARCHAR(100) NOT NULL DEFAULT '' COMMENT '名字',
    `PASSWORD`        VARCHAR(100) NOT NULL DEFAULT '' COMMENT '密码',
    `LAST_LOGIN_TIME` TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '上次登录时间',
    `INSERT_DATA`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `UPDATE_DATA`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `DEL`             BOOLEAN      NOT NULL DEFAULT FALSE COMMENT '是否删除,0:否,1:是,默认否',
    PRIMARY KEY (`ID`)
) ENGINE = INNODB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;