-- MySQL Script generated by MySQL Workbench
-- mar 22 oct 2019 19:20:11 CST
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema KRONOS
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `KRONOS` ;

-- -----------------------------------------------------
-- Schema KRONOS
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `KRONOS` DEFAULT CHARACTER SET utf8 ;
USE `KRONOS` ;

-- -----------------------------------------------------
-- Table `KRONOS`.`T_ACT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_ACT` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_ACT` (
  `INDEX` INT(11) NOT NULL AUTO_INCREMENT,
  `ACTNUMBER` VARCHAR(45) NOT NULL,
  `SESSIONTYPE` VARCHAR(45) CHARACTER SET 'armscii8' NULL,
  `SESSIONDATE` DATE NOT NULL,
  `URL` VARCHAR(100) NULL,
  `PUBLIC` TINYINT NULL,
  `ACTIVE` TINYINT NULL,
  UNIQUE INDEX `DATE_UNIQUE` (`SESSIONDATE` ASC),
  UNIQUE INDEX `ACTNUMBER_UNIQUE` (`ACTNUMBER` ASC),
  PRIMARY KEY (`INDEX`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_STATE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_STATE` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_STATE` (
  `ID` INT NOT NULL,
  `DESCRIPTION` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_TYPE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_TYPE` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_TYPE` (
  `ID` CHAR(1) NOT NULL,
  `DESCRIPTION` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_ACCORD`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_ACCORD` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_ACCORD` (
  `INDEX` INT(11) NOT NULL AUTO_INCREMENT,
  `ACCNUMBER` VARCHAR(45) NOT NULL,
  `INCORDATE` DATE NOT NULL,
  `INCORTIME` TIME NULL,
  `DEADLINE` DATE NOT NULL,
  `SESSIONDATE` DATE NOT NULL,
  `OBSERVATIONS` LONGTEXT NULL,
  `PUBLIC` TINYINT NULL,
  `NOTIFIED` TINYINT NULL,
  `STATE` INT NOT NULL,
  `TYPE_ID` CHAR(1) NOT NULL,
  PRIMARY KEY (`INDEX`),
  UNIQUE INDEX `ACCNUMBER_UNIQUE` (`ACCNUMBER` ASC),
  INDEX `fk_T_ACCORD_T_STATE1_idx` (`STATE` ASC),
  INDEX `fk_T_ACCORD_T_TYPE1_idx` (`TYPE_ID` ASC))
ENGINE = MyISAM
DEFAULT CHARACTER SET = latin2
COLLATE = latin2_hungarian_ci;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_USER` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_USER` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `DEPARTMENT` INT NOT NULL,
  `PASSWORD` VARCHAR(45) NULL,
  `T_TEMPUSER` VARCHAR(45) NOT NULL,
  UNIQUE INDEX `T_TEMPUSER_UNIQUE` (`T_TEMPUSER` ASC),
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_DEPARTMENT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_DEPARTMENT` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_DEPARTMENT` (
  `ID` INT NOT NULL,
  `NAME` VARCHAR(100) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_ROLE`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_ROLE` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_ROLE` (
  `USERID` VARCHAR(100) NOT NULL,
  `ROLE` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`USERID`, `ROLE`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_NOTIFICATION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_NOTIFICATION` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_NOTIFICATION` (
  `ACCORD` VARCHAR(45) NOT NULL,
  `T_USER_T_TEMPUSER` VARCHAR(45) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_TEMPUSER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_TEMPUSER` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_TEMPUSER` (
  `INDEX` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NULL,
  `EMAIL` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`INDEX`),
  UNIQUE INDEX `EMAIL_UNIQUE` (`EMAIL` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_USERACC`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_USERACC` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_USERACC` (
  `TEMPUSER` VARCHAR(45) NOT NULL,
  `ACCORD` VARCHAR(45) NOT NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_ACCPDF`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_ACCPDF` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_ACCPDF` (
  `ACCORD` VARCHAR(45) NOT NULL,
  `URL` VARCHAR(100) NULL,
  `FINALRESPONSE` TINYINT(1) NULL)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `KRONOS`.`T_ACCDPRMNT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `KRONOS`.`T_ACCDPRMNT` ;

CREATE TABLE IF NOT EXISTS `KRONOS`.`T_ACCDPRMNT` (
  `DEPARTMENT` INT NOT NULL,
  `ACCORD` VARCHAR(45) NOT NULL)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
