create database KRONOS;
use KRONOS;

create table T_STATE (
ID int,
DESCRIPTION varchar(45), 
constraint PK_STATE primary key (ID)
);

create table T_TYPE (
ID char(1), 
DESCRIPTION varchar(25),
constraint PK_TYPE primary key (ID)
);

create table T_ACT(
INDX int auto_increment,
SESSIONTYPE varchar(45),
SESSIONDATE date, 
URL varchar(100),
PUBLIC tinyint,
ACTIVE tinyint,
constraint PK_ACT primary key (INDX),
constraint UK_SESSIONDATE unique key (SESSIONDATE)
);

create table T_DEPARTMENT (
ID int, 
NAME varchar(100),
constraint PK_DEPARMENT primary key(ID)
);

create table T_TEMPUSER(
INDX int auto_increment,
NAME varchar(45),
EMAIL varchar(45),
constraint PK_TEMPUSER primary key (INDX),
constraint UK_EMAIL unique key(EMAIL)
); 

create table T_USER(
ID int auto_increment, 
DEPARTMENT int,
PASSWORD varchar(100),
TEMPUSER varchar(45),
constraint PK_USER primary key (ID),
constraint FK_TEMPUSER foreign key (TEMPUSER) references T_TEMPUSER(EMAIL),
constraint FK_DEPARTMENT foreign key (DEPARTMENT) references T_DEPARTMENT(ID)
);


create table T_ACCORD(
INDX int auto_increment,
ACCNUMBER varchar(45),
INCORDATE date,
INCORTIME time,
DEADLINE date,
SESSIONDATE date,
OBSERVATIONS longtext,
PUBLIC tinyint,
NOTIFIED tinyint,
STATE int,
TYPE_ID char(1),
constraint PK_ACCORD primary key (INDX),
constraint FK_SESSIONDATE foreign key (SESSIONDATE) references T_ACT(SESSIONDATE),
constraint FK_TYPE foreign key (TYPE_ID) references T_TYPE(ID),
constraint FK_STATE foreign key (STATE) references T_STATE(ID),
constraint UK_ACCNUMBER unique key (ACCNUMBER)
);


create table T_ACCPDF(
ACCORD varchar(45),
URL varchar(100),
FINALRESPONSE boolean,
constraint FK_ACCORD foreign key (ACCORD) references T_ACCORD(ACCNUMBER)
);


create table T_USERACC(
TEMPUSER varchar(45),
ACCORD varchar(45),
constraint FK_TEMPUSER1 foreign key (TEMPUSER) references T_TEMPUSER(EMAIL),
constraint FK_ACCORD1 foreign key (ACCORD) references T_ACCORD(ACCNUMBER)
);

create table T_NOTIFICATION(
ACCORD varchar(45),
USER varchar(45),
constraint FK_ACCORD2 foreign key(ACCORD) references T_ACCORD(ACCNUMBER),
constraint FK_USER foreign key (USER) references T_USER(TEMPUSER)
);


create table T_ACCDPRMNT(
DEPARTMENT int,
ACCORD varchar(45),
constraint FK_DEPARTMENT1 foreign key (DEPARTMENT) references T_DEPARTMENT(ID),
constraint FK_ACCORD3 foreign key (ACCORD) references T_ACCORD(ACCNUMBER)
);


DROP TABLE IF EXISTS `KRONOS`.`T_ROLE` ;
CREATE TABLE IF NOT EXISTS `KRONOS`.`T_ROLE` (
ROLE_NAME varchar(45),
constraint pk1 primary key(ROLE_NAME)
);

DROP TABLE IF EXISTS `KRONOS`.`T_USERROLE` ;
CREATE TABLE IF NOT EXISTS `KRONOS`.`T_USERROLE` (
USER_NAME varchar(45),
ROLE_NAME varchar(45),
constraint pk1 primary key(USER_NAME,ROLE_NAME)
);

USE `KRONOS`;
DELIMITER ;
CREATE TABLE IF NOT EXISTS `KRONOS`.`T_DELETEDACCORDS`(
ACCORDNUMBER varchar(45) NOT NULL,
USER varchar(45) NOT NULL,
Date DATETIME NOT NULL	
);
