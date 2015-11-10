-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 5.35.247.12    Database: sporty
-- ------------------------------------------------------
-- Server version	5.5.46-0ubuntu0.14.04.2

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DepartmentTeam`
--

DROP TABLE IF EXISTS `DepartmentTeam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DepartmentTeam` (
  `changeMe` int(12) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DepartmentTeam`
--

LOCK TABLES `DepartmentTeam` WRITE;
/*!40000 ALTER TABLE `DepartmentTeam` DISABLE KEYS */;
/*!40000 ALTER TABLE `DepartmentTeam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `department` (
  `departmentId` int(12) NOT NULL AUTO_INCREMENT,
  `sport` varchar(255) DEFAULT NULL,
  `headId` int(12) DEFAULT NULL COMMENT 'foreign key für personId',
  PRIMARY KEY (`departmentId`),
  UNIQUE KEY `departmentId_UNIQUE` (`departmentId`),
  KEY `headId_idx` (`headId`),
  CONSTRAINT `fkDepartmentMember` FOREIGN KEY (`headId`) REFERENCES `member` (`memberId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `department`
--

LOCK TABLES `department` WRITE;
/*!40000 ALTER TABLE `department` DISABLE KEYS */;
INSERT INTO `department` VALUES (1000,'Soccer',2001),(1001,'Volleyball',2000),(1002,'Baseball',2002),(1003,'Football',2003);
/*!40000 ALTER TABLE `department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `league`
--

DROP TABLE IF EXISTS `league`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `league` (
  `leagueId` int(12) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`leagueId`),
  UNIQUE KEY `leagueId_UNIQUE` (`leagueId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Liga';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `league`
--

LOCK TABLES `league` WRITE;
/*!40000 ALTER TABLE `league` DISABLE KEYS */;
/*!40000 ALTER TABLE `league` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `match`
--

DROP TABLE IF EXISTS `match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `match` (
  `matchId` int(12) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`matchId`),
  UNIQUE KEY `matchId_UNIQUE` (`matchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Wettkämpfe';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `match`
--

LOCK TABLES `match` WRITE;
/*!40000 ALTER TABLE `match` DISABLE KEYS */;
/*!40000 ALTER TABLE `match` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matchResult`
--

DROP TABLE IF EXISTS `matchResult`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `matchResult` (
  `matchResultId` int(12) NOT NULL,
  PRIMARY KEY (`matchResultId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Wettkampf - Ergebnisse';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matchResult`
--

LOCK TABLES `matchResult` WRITE;
/*!40000 ALTER TABLE `matchResult` DISABLE KEYS */;
/*!40000 ALTER TABLE `matchResult` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `memberId` int(12) NOT NULL AUTO_INCREMENT,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `gender` enum('M','F') DEFAULT NULL,
  `dateOfBirth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `squad` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL COMMENT 'roles: \nplayer (Spieler), trainer (Trainer), management (Vorstand), departmentHead (Abteilungsleiter) ',
  `username` varchar(255) DEFAULT NULL,
  `isFeePaid` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`memberId`),
  UNIQUE KEY `memberId_UNIQUE` (`memberId`)
) ENGINE=InnoDB AUTO_INCREMENT=2185 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (2000,'Marion','Kennedy','F',NULL,NULL,NULL,NULL,'Department Head',NULL,0),(2001,'George','Alexander','M',NULL,NULL,NULL,NULL,'Department Head',NULL,0),(2002,'Johanna','Howard','F',NULL,NULL,NULL,NULL,'Department Head',NULL,0),(2003,'Myra','Fox','F',NULL,NULL,NULL,NULL,'Department Head',NULL,0),(2004,'Tina','Pirelli','F',NULL,NULL,NULL,NULL,'Trainer',NULL,0),(2005,'Patrick','Lyons','M',NULL,NULL,NULL,NULL,'Trainer',NULL,0),(2006,'Patricia','Delgado','F',NULL,NULL,NULL,NULL,'Trainer',NULL,0),(2007,'Kirk','Summers','M',NULL,NULL,NULL,NULL,'Trainer',NULL,0),(2008,'Arthur','Mcdaniel','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2009,'Clayton','Perez','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2010,'Claudia','Mckenzie','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2011,'Williamghfg','Shakespeare','M','1950-05-10',NULL,NULL,NULL,NULL,NULL,0),(2012,'Opal','Carpenter','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2013,'Mildred','Mccormick','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2014,'Julian','Price','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2015,'Patrick','Gomez','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2016,'Sheldon','Gonzales','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2017,'Minnie','Torres','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2018,'Kelly','Herrera','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2019,'Rufus','Griffith','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2020,'Josefina','Banks','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2021,'Rafael','Adkins','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2022,'Andre','Bowers','M','1960-11-30',NULL,NULL,NULL,NULL,NULL,0),(2023,'Jeanette','Goodman','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2024,'Wendell','Myers','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2025,'Julia','Rodriquez','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2026,'Jamie','Diaz','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2027,'Eula','Hudson','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2028,'Isabel','Ortiz','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2029,'Albert','Barnett','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2030,'Josephine','Crawford','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2031,'Milton','Poole','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2032,'Eileen','Gordon','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2033,'Judy','Riley','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2034,'Brian','Nichols','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2035,'Joel','Arnold','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2036,'Kristin','Burke','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2037,'Lydia','Page','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2038,'Stephanie','Lopez','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2039,'Dianne','Padilla','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2040,'Vernon','Ramsey','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2041,'Stella','Colon','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2042,'Norman','Ballard','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2043,'Dora','Mendez','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2044,'Krystal','Daniels','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2045,'Lindsey','Lamb','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2046,'Delbert','Becker','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2047,'Tanya','Cummings','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2048,'Gregory','Luna','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2049,'Elisa','Potter','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2050,'Marie','Ellis','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2051,'Hugo','Ramirez','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2052,'Wilbur','Houston','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2053,'Howard','Henderson','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2054,'Loretta','Cooper','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2055,'Eugene','Kelley','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2056,'Darrell','Day','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2057,'Tracy','Wheeler','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2058,'Lorenzo','Wilkerson','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2059,'Alberta','Riley','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2060,'Edward','Cross','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2061,'Edwina','Patterson','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2062,'Carlos','Singleton','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2063,'Silvia','Sanders','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2064,'Julia','Lyons','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2065,'Lorena','Swanson','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2066,'Darlene','Underwood','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2067,'Elijah','Miller','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2068,'Nino','Padilla','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2069,'Edmund','Jacobs','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2070,'Sylvester','Turner','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2071,'Meryl','Roy','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2072,'Andrew','Holland','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2073,'Jaqueline','Mack','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2074,'Alexandra','Collier','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2075,'Carl','Ryan','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2076,'Jermaine','Collins','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2077,'Bill','Marshall','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2078,'Sylvia','Warner','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2079,'Emilio','Wilkins','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2080,'Einar','Carroll','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2081,'Roland','Casey','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2082,'Trevor','Jefferson','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2083,'Sally','Schultz','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2084,'Charlotte','Mackenzie','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2085,'Keith','Williams','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2086,'Shawna','Rivera','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2087,'Irina','Castro','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2088,'Martha','Dunn','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2089,'Rosie','Phillips','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2090,'Diana','Cole','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2091,'Vincento','Lombardi','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2092,'Muriel','Kim','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2093,'Annie','Stevens','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2094,'Gina','Hudson','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2095,'Tara','Sherman','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2096,'Sharon','Logan','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2097,'Stewart','Hale','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2098,'Janis','Ball','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2099,'Gaia','Sesto','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2100,'Leonarda','Albano','F',NULL,NULL,NULL,NULL,NULL,NULL,0),(2101,'Andrej','Borna','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2102,'Kristofor','Jadrankovic','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2103,'Branko','Stevan','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2104,'Franjo','Antovic','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2105,'Javor','Milenko','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2106,'Björn','Petersson','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2107,'Valdemar','Melker','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2108,'Hannes','Konrad','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2109,'Theodor','Stellan','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2110,'Nils','Magnussen','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2111,'Miron','Avramenko','M',NULL,NULL,NULL,NULL,NULL,NULL,0),(2117,'Annalisa','Field','F','2000-12-10','test@gmx.at','Street 1',NULL,NULL,NULL,0),(2127,'Sergii','Maidanov','M','1995-03-22',NULL,NULL,NULL,NULL,NULL,0),(2128,'Gordon','Freeman','M','1970-05-20',NULL,NULL,NULL,NULL,NULL,0),(2130,'tewasdas','asdasd','M','1990-08-10',NULL,NULL,NULL,NULL,NULL,0),(2132,'Karoline','Deuring','F','1982-08-13',NULL,'6700 Bludenz',NULL,NULL,NULL,0),(2150,'Fred','Tester','F',NULL,'test002@gmx.at','Street 1',NULL,NULL,NULL,1),(2153,'Fred','Tester','F',NULL,'test002@gmx.at','Street 1',NULL,NULL,NULL,1),(2156,'Fred','Tester','F',NULL,'test002@gmx.at','Street 1',NULL,NULL,NULL,1),(2159,'Fred','Tester','F',NULL,'test002@gmx.at','Street 1',NULL,NULL,NULL,1),(2162,'Fred','Tester','F',NULL,'test002@gmx.at','Street 1',NULL,NULL,NULL,1),(2165,'Fred','Tester','F',NULL,'test002@gmx.at','Street 1',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `team`
--

DROP TABLE IF EXISTS `team`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `team` (
  `teamId` int(12) NOT NULL AUTO_INCREMENT,
  `trainerId` int(12) DEFAULT NULL,
  `departmentId` int(12) DEFAULT NULL COMMENT 'foreign key für sport / department',
  `leagueId` int(12) DEFAULT NULL,
  `teamName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`teamId`),
  UNIQUE KEY `teamId_UNIQUE` (`teamId`),
  KEY `trainerId_idx` (`trainerId`),
  KEY `departmentId_idx` (`departmentId`),
  CONSTRAINT `fkTeamDepartment` FOREIGN KEY (`departmentId`) REFERENCES `department` (`departmentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fkTeamMember` FOREIGN KEY (`trainerId`) REFERENCES `member` (`memberId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1008 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `team`
--

LOCK TABLES `team` WRITE;
/*!40000 ALTER TABLE `team` DISABLE KEYS */;
INSERT INTO `team` VALUES (1004,2004,1000,NULL,'Incredible Kickers'),(1005,2005,1001,NULL,'Milas Superstars'),(1006,2006,1002,NULL,'Team Rocket'),(1007,2007,1003,NULL,'DB Sparrows');
/*!40000 ALTER TABLE `team` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-11-10  9:42:55
