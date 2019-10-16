USE `KRONOS`;
DROP procedure IF EXISTS insertAccord;
DELIMITER $$
USE `KRONOS`$$
CREATE PROCEDURE insertAccord (IN accNumber VARCHAR(45), IN incorDate DATE, 
IN deadLine DATE, IN sessionDate DATE, IN type_id
CHAR(1), IN observations longtext, IN publics TINYINT(4),
IN notified TINYINT(4), IN states INT, IN notifDate date)
BEGIN
INSERT INTO T_ACCORD (ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, TYPE_ID, NOTIFDATE) 
VALUES (accNumber, incorDate, deadLine, sessionDate, observations, publics, notified, states, type_id, notifDate);
commit; 
END$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS insertAccPdf;
DELIMiTER $$
USE `KRONOS`$$
create procedure insertAccPdf(
in accord varchar(45), in url varchar(100))
begin
insert into T_ACCPDF (ACCORD, URL) values (accord, url);
commit;  
end$$ 
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS insertTempUser;
DELIMITER $$
USE `KRONOS`$$
create procedure insertTempUser(
in namee varchar(45), in email varchar(45))
begin
insert into T_TEMPUSER (NAME, EMAIL) values (namee, email);
commit; 
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS insertUserAcc;
DELIMITER $$
USE `KRONOS`$$
create procedure insertUserAcc(
in tempuser varchar(45), in accord varchar(45))
begin
insert into T_USERACC (TEMPUSER, ACCORD) values (tempuser, accord);
commit; 
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchAccordType;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordType(in type_id char(1))
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, NOTIFDATE, T_ACCPDF.URL  from T_ACCORD, T_ACCPDF where ACCNUMBER= T_ACCPDF.ACCORD and T_ACCORD.TYPE_ID = type_id;
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchAccordNumber;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordNumber(in accNumber varchar(45)
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, NOTIFDATE, T_ACCPDF.URL  from T_ACCORD, T_ACCPDF where ACCNUMBER= T_ACCPDF.ACCORD and T_ACCORD.ACCNUMBER = accNumber;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS searchAccordIncorDate;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordIncorDate(in incorDate  date
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, NOTIFDATE, T_ACCPDF.URL  from T_ACCORD, T_ACCPDF where ACCNUMBER= T_ACCPDF.ACCORD and T_ACCORD.INCORDATE = incorDate;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS searchAccordsessionDate;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAccordsessionDate(in sessionDate date
)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, NOTIFDATE, T_ACCPDF.URL  from T_ACCORD, T_ACCPDF where ACCNUMBER= T_ACCPDF.ACCORD and T_ACCORD.SESSIONDATE = sessionDate;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS deleteAccord;
DELIMITER $$
USE `KRONOS`$$
create procedure deleteAccord(in accord varchar(45)  
)
begin
delete from T_USERACC where T_USERACC.ACCORD = accord;
delete from T_ACCPDF where T_ACCPDF.ACCORD = accord;
delete from T_ACCORD where T_ACCORD.ACCNUMBER = accord; 
end$$
DELIMITER ; 

DROP TABLE IF EXISTS `KRONOS`.`T_DELETEDACCORDS` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_DELETEDACCORDS` (
  `ACCORD` VARCHAR(45) NOT NULL,
  action VARCHAR(50) DEFAULT NULL
  )
ENGINE = InnoDB;

USE `KRONOS`;
DROP procedure IF EXISTS searchAllAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllAccords()
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, NOTIFDATE,  T_ACCPDF.URL  from T_ACCORD, T_ACCPDF where T_ACCORD.ACCNUMBER= T_ACCPDF.ACCORD;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS searchTempUser;
DELIMITER $$
USE `KRONOS`$$
create procedure searchTempUser( in email varchar(45))
begin
select NAME, EMAIL  from T_TEMPUSER where EMAIL= email; 
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordSessionDate;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordSessionDate( in accord varchar(45), in newdate date)
begin
update T_ACCORD set SESSIONDATE = newdate where ACCNUMBER = accord;   
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordDeadline;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordDeadline( in accord varchar(45), in newdeadline date)
begin
update T_ACCORD set DEADLINE = newdeadline where ACCNUMBER = accord;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordType;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordType( in accord varchar(45), in newtype char(1))
begin
update T_ACCORD set TYPE_ID = newtype where ACCNUMBER = accord;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordState;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordState( in accord varchar(45), in newstate int)
begin
update T_ACCORD set STATE = newstate where ACCNUMBER = accord;
end$$
DELIMITER ;  

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordUser;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordUser( in accord varchar(45), in newuser varchar(45), in antuser varchar(45))
begin
update T_USERACC set TEMPUSER = newuser where ACCORD = accord and TEMPUSER= antuser;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordUrls;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordUrls( in accord varchar(45), in newurl varchar(100), in anturl varchar(100))
begin
update T_ACCPDF set URL=newurl where ACCORD=accord and URL=anturl; 
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS updateAccordUrlResponse;
DELIMITER $$
USE `KRONOS`$$
create procedure updateAccordUrlResponse( in accord varchar(45), in url varchar(100), in finalResponse bool)
begin
update T_ACCPDF set FINALRESPONSE=finalResponse where ACCORD=accord and URL=anturl; 
end$$
DELIMITER ;

USE `KRONOS`;
DROP procedure IF EXISTS searchAllStates;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllStates()
begin
select  ID , DESCRIPTION from T_STATE;
end$$
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS searchAllTypes;
DELIMITER $$
USE `KRONOS`$$
create procedure searchAllTypes()
begin
select  ID , DESCRIPTION from T_TYPE;
end$$
DELIMITER ; 
                                                                                                    
USE `KRONOS`;
DROP procedure IF EXISTS searchExpiredAccords;
DELIMITER $$
USE `KRONOS`$$
create procedure searchExpiredAccords(in actual date, in limt date)
begin
select ACCNUMBER, INCORDATE, 
DEADLINE, SESSIONDATE, TYPE_ID, OBSERVATIONS, PUBLIC, NOTIFIED,  STATE, NOTIFDATE, T_ACCPDF.URL  from T_ACCORD, T_ACCPDF where ACCNUMBER= T_ACCPDF.ACCORD and T_ACCORD.DEADLINE <= '2019-10-14' and T_ACCORD.DEADLINE >= '2019-09-14';
end$$
<<<<<<< HEAD
DELIMITER ; 
=======
DELIMITER ; 

USE `KRONOS`;
DROP procedure IF EXISTS deletePdf;
DELIMiTER $$
USE `KRONOS`$$
create procedure deletePdf(
in accord varchar(45), in url varchar(100))
begin
delete from T_ACCPDF where ACCORD=accord and URL= url; 
commit;  
end$$ 
DELIMITER ;
>>>>>>> 7aa3af2cb58ca80fb8bd5923397f38b76bbdc481
