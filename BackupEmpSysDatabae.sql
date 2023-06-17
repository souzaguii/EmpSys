-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: empsysdatabase
-- ------------------------------------------------------
-- Server version	8.0.33

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `despezas`
--

DROP TABLE IF EXISTS `despezas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `despezas` (
  `idDes` int NOT NULL AUTO_INCREMENT,
  `descricaoDes` varchar(100) DEFAULT NULL,
  `precoDes` double DEFAULT NULL,
  `statusDes` int DEFAULT '0',
  `dataDes` date DEFAULT NULL,
  `dataconclusaoDes` date DEFAULT NULL,
  PRIMARY KEY (`idDes`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `despezas`
--

LOCK TABLES `despezas` WRITE;
/*!40000 ALTER TABLE `despezas` DISABLE KEYS */;
/*!40000 ALTER TABLE `despezas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entradas`
--

DROP TABLE IF EXISTS `entradas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `entradas` (
  `idEnt` int NOT NULL AUTO_INCREMENT,
  `idTipSer` int DEFAULT NULL,
  `idEst` int DEFAULT NULL,
  `codigoEnt` varchar(6) DEFAULT NULL,
  `dataEnt` date DEFAULT NULL,
  `precoEnt` double DEFAULT NULL,
  `detalhesEnt` varchar(255) DEFAULT NULL,
  `quantidadeEnt` int DEFAULT NULL,
  `idTip` int NOT NULL,
  PRIMARY KEY (`idEnt`),
  KEY `fk_entradas_estoque_idx` (`idEst`),
  KEY `fk_entradas_tiposervico_idx` (`idTipSer`),
  CONSTRAINT `fk_entradas_estoque` FOREIGN KEY (`idEst`) REFERENCES `estoque` (`idEst`),
  CONSTRAINT `fk_entradas_tiposervico` FOREIGN KEY (`idTipSer`) REFERENCES `tiposervico` (`idTipSer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entradas`
--

LOCK TABLES `entradas` WRITE;
/*!40000 ALTER TABLE `entradas` DISABLE KEYS */;
/*!40000 ALTER TABLE `entradas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estoque`
--

DROP TABLE IF EXISTS `estoque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estoque` (
  `idEst` int NOT NULL AUTO_INCREMENT,
  `tipoprodutoEst` varchar(50) DEFAULT NULL,
  `modeloEst` varchar(100) DEFAULT NULL,
  `marcaEst` varchar(100) DEFAULT NULL,
  `corEst` varchar(50) DEFAULT NULL,
  `materialEst` varchar(50) DEFAULT NULL,
  `detalhesEst` varchar(255) DEFAULT NULL,
  `localizacaoEst` varchar(50) DEFAULT NULL,
  `precoEst` double DEFAULT NULL,
  `quantidadeEst` int DEFAULT NULL,
  `tipochipEst` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`idEst`)
) ENGINE=InnoDB AUTO_INCREMENT=532 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estoque`
--

LOCK TABLES `estoque` WRITE;
/*!40000 ALTER TABLE `estoque` DISABLE KEYS */;
INSERT INTO `estoque` VALUES (2,'Película','K10 Pro','LG','','Vidro','','Caixa 2',10,4,NULL),(3,'Película','K5','Lenovo','','Vidro','','Caixa 2',10,2,NULL),(4,'Película','K51S','LG','Preto','Vidro','','Caixa 2',20,2,NULL),(5,'Película','K10 2017','LG','','Vidro','','Caixa 2',10,7,NULL),(6,'Película','K8','LG','','Vidro','','Caixa 2',10,1,NULL),(7,'Película','K10 2016','LG','','Vidro','','Caixa 2',10,3,NULL),(8,'Película','K9','LG','','Vidro','','Caixa 2',10,1,NULL),(9,'Película','K12 Plus','LG','','Vidro','','Caixa 2',10,4,NULL),(10,'Película','K11 Plus','LG','','Vidro','','Caixa 2',10,3,NULL),(11,'Película','K41S','LG','Preto','Vidro','','Caixa 2',20,2,NULL),(12,'Película','K4 2017','LG','','Vidro','','Caixa 2',10,3,NULL),(13,'Película','K50S','LG','Preto','Vidro','','Caixa 2',20,3,NULL),(14,'Película','K10 Power','LG','','Vidro','','Caixa 2',10,6,NULL),(15,'Película','X3','Xiaomi Pocophone','Preto','Vidro','','Caixa 2',20,5,NULL),(16,'Película','Mi A2 Lite','Xiaomi','Preto','Vidro','','Caixa 2',20,5,NULL),(17,'Película','Mi 8 Pro','Xiaomi','Preto','Vidro','','Caixa 2',20,3,NULL),(18,'Película','Mi 6','Xiaomi','','Vidro','','Caixa 2',10,2,NULL),(19,'Película','Mi A2','Xiaomi','Preto','Vidro','','Caixa 2',20,1,NULL),(20,'Película','Mi 5','Xiaomi','','Vidro','','Caixa 2',10,3,NULL),(21,'Película','6A','Xiaomi Redmi','Preto','Vidro','','Caixa 2',20,5,NULL),(22,'Película','Note 6 Pro','Xiaomi Redmi','Preto','Vidro','','Caixa 2',20,2,NULL),(23,'Película','Mi 9 SE','Xiaomi','Preto','Vidro','','Caixa 2',20,3,NULL),(24,'Película','Note 10','Xiaomi Redmi','Preto','Vidro','','Caixa 2',20,9,NULL),(25,'Película','Mi 8','Xiaomi','Preto','Vidro','','Caixa 2',20,3,NULL),(26,'Película','6 Pro','Xiaomi Redmi','Preto','Vidro','','Caixa 2',20,1,NULL),(27,'Película','9S','Xiaomi Redmi','Preto','Vidro','','Caixa 2',20,13,NULL),(28,'Película','K12 Prime','LG','','Vidro','','Caixa 2',10,2,NULL),(29,'Película','Galaxy M02s','Samsung','Preto','Vidro','','Caixa 1',20,2,NULL),(30,'Película','Galaxy A02s','Samsung','Preto','Vidro','','Caixa 1',20,3,NULL),(31,'Película','Galaxy A01','Samsung','Preto','Vidro','','Caixa 1',20,5,NULL),(32,'Película','Galaxy J5 Pro','Samsung','Branco','Plástico','','Caixa 1',10,1,NULL),(33,'Película','Galaxy J5 Pro','Samsung','Preto','Plástico','','Caixa 1',10,1,NULL),(34,'Película','Galaxy S7','Samsung','Preto','Plástico','','Caixa 1',10,1,NULL),(35,'Película','Galaxy A9 2016','Samsung','','Vidro','','Caixa 1',10,2,NULL),(36,'Película','Galaxy S9 Plus','Samsung','Preto','Vidro','','Caixa 1',20,4,NULL),(37,'Película','Galaxy M31s','Samsung','Preto','Vidro','','Caixa 1',20,4,NULL),(38,'Película','Galaxy A01 Core','Samsung','Preto','Vidro','','Caixa 1',20,4,NULL),(39,'Película','Galaxy A52','Samsung','Preto','Vidro','','Caixa 1',20,2,NULL),(40,'Película','Galaxy A20s','Samsung','Preto','Vidro','','Caixa 1',20,3,NULL),(41,'Película','Galaxy M10','Samsung','','Vidro','','Caixa 1',10,5,NULL),(42,'Película','Galaxy M30','Samsung','','Vidro','','Caixa 1',10,3,NULL),(43,'Película','Galaxy J8','Samsung','Preto','Vidro','','Caixa 1',20,2,NULL),(44,'Película','Galaxy J8','Samsung','','Vidro','','Caixa 1',10,20,NULL),(45,'Película','Galaxy A7 2017','Samsung','','Vidro','','Caixa 1',10,5,NULL),(46,'Película','Galaxy J4','Samsung','','Vidro','','Caixa 1',10,5,NULL),(47,'Película','Galaxy M51','Samsung','Preto','Vidro','','Caixa 1',20,2,NULL),(48,'Película','Galaxy S20','Samsung','Preto','Vidro','','Caixa 1',20,3,NULL),(49,'Película','Galaxy A8 Plus','Samsung','','Vidro','','Caixa 1',10,1,NULL),(50,'Película','Galaxy J1 2016','Samsung','','Vidro','','Caixa 1',10,3,NULL),(51,'Película','Galaxy J4 Plus','Samsung','Preto','Vidro','','Caixa 1',20,5,NULL),(52,'Película','Galaxy A71','Samsung','','Vidro','','Caixa 1',10,2,NULL),(53,'Película','Galaxy A6','Samsung','','Vidro','','Caixa 1',10,2,NULL),(54,'Película','Galaxy J1 Mini','Samsung','','Vidro','','Caixa 1',10,2,NULL),(55,'Película','Galaxy A70','Samsung','','Vidro','','Caixa 1',10,4,NULL),(56,'Película','Galaxy S21','Samsung','Preto','Vidro','','Caixa 1',20,1,NULL),(57,'Película','Galaxy J5 Prime','Samsung','Branco','Vidro','','Caixa 1',20,2,NULL),(58,'Película','Galaxy J5 Prime','Samsung','','Vidro','','Caixa 1',10,2,NULL),(59,'Película','Galaxy A7 2016','Samsung','','Vidro','','Caixa 1',10,5,NULL),(60,'Película','Galaxy A22 5G','Samsung','Preto','Vidro','','Caixa 1',20,3,NULL),(61,'Película','Galaxy S22','Samsung','Preto','Vidro','','Caixa 1',20,5,NULL),(62,'Película','Galaxy A80/A90','Samsung','Preto','Vidro','','Caixa 1',20,4,NULL),(63,'Película','Galaxy A11','Samsung','Preto','Vidro','','Caixa 1',20,13,NULL),(64,'Película','Galaxy S21 Plus','Samsung','Preto','Vidro','','Caixa 1',20,2,NULL),(65,'Película','Galaxy J6','Samsung','Branco','Plástico','','Caixa 1',10,1,NULL),(66,'Película','Galaxy A5','Samsung','','Vidro','','Caixa 1',10,1,NULL),(67,'Película','Galaxy J7 Max','Samsung','','Vidro','','Caixa 1',10,4,NULL),(68,'Película','Galaxy J4','Samsung','Preto','Vidro','','Caixa 1',20,4,NULL),(69,'Película','Galaxy S8 Plus','Samsung','','Vidro','','Caixa 1',10,4,NULL),(70,'Película','Galaxy A21s','Samsung','Preto','Vidro','','Caixa 1',20,4,NULL),(71,'Película','Galaxy S21','Samsung','Preto','Vidro','','Caixa 1',20,1,NULL),(72,'Película','Galaxy A31','Samsung','Preto','Vidro','','Caixa 1',20,3,NULL),(73,'Película','Galaxy J6 Plus','Samsung','Preto','Vidro','','Caixa 1',20,3,NULL),(74,'Película','Galaxy J7 Pro','Samsung','Branco','Vidro','','Caixa 1',20,1,NULL),(213,'Capinha','Note 10/10S','Xiaomi Redmi','Verde','','','Mostruário',20,1,NULL),(214,'Capinha','Note 10/10S','Xiaomi Redmi','Rosa','','','Mostruário',20,1,NULL),(215,'Capinha','Note 10/10S','Xiaomi Redmi','Amarelo','','','Mostruário',20,1,NULL),(216,'Capinha','Note 10/10S','Xiaomi Redmi','Azul','','','Mostruário',20,1,NULL),(217,'Capinha','Note 10/10S','Xiaomi Redmi','Laranja','','','Mostruário',20,1,NULL),(218,'Capinha','Note 10/10S','Xiaomi Redmi','Vinho','','','Mostruário',20,1,NULL),(219,'Capinha','Note 10/10S','Xiaomi Redmi','Verde Água','','','Mostruário',20,1,NULL),(220,'Capinha','Note 10/10S','Xiaomi Redmi','Branco','','','Mostruário',20,1,NULL),(221,'Capinha','Note 10/10S','Xiaomi Redmi','Lilás','','','Mostruário',20,1,NULL),(222,'Capinha','Note 7','Xiaomi Redmi','Preto','','','Caixa 1 - Xiaomi',15,4,NULL),(223,'Capinha','Note 7','Xiaomi Redmi','Cinza','','','Caixa 1 - Xiaomi',15,4,NULL),(224,'Capinha','Note 10 5G','Xiaomi Redmi','Verde','','','Mostruário',20,1,NULL),(225,'Capinha','Note 10 5G','Xiaomi Redmi','Preto','','','Mostruário',20,1,NULL),(226,'Capinha','Note 10 5G','Xiaomi Redmi','Vermelho','','','Mostruário',20,1,NULL),(227,'Capinha','M3','Xiaomi Pocophone','Preto','','','Mostruário',20,1,NULL),(228,'Capinha','M3','Xiaomi Pocophone','Rosa','','','Mostruário',20,2,NULL),(229,'Capinha','Galaxy S10e','Samsung','Transparente','','','Caixa 1 - Samsung',15,1,NULL),(230,'Capinha','Galaxy J5 Prime','Samsung','Preto','','','Caixa 1 - Samsung',15,3,NULL),(234,'Capinha','Galaxy A32','Samsung','Rosa','','','Mostruário',20,4,NULL),(237,'Capinha','Galaxy S20 Ultra','Samsung','Transparente','','','Caixa 1 - Samsung',20,3,NULL),(238,'Capinha','Galaxy A01','Samsung','Transparente','','','Caixa 1 - Samsung',15,3,NULL),(239,'Capinha','Galaxy A01','Samsung','Rosa','','Desenho de flor.','Caixa 1 - Samsung',15,1,NULL),(240,'Capinha','Galaxy A02s','Samsung','Preto','','','Mostruário',20,1,NULL),(241,'Capinha','Galaxy J6 Prime','Samsung','Preto','','','Caixa 1 - Samsung',15,1,NULL),(242,'Capinha','Galaxy A10','Samsung','Verde Claro','','','Mostruário',20,2,NULL),(243,'Capinha','Galaxy A10','Samsung','Rosa','','','Mostruário',20,1,NULL),(244,'Capinha','Galaxy S10e','Samsung','Verde','','','Caixa 1 - Samsung',15,1,NULL),(245,'Capinha','Galaxy S9','Samsung','Preto','','','Caixa 1 - Samsung',15,1,NULL),(246,'Capinha','Galaxy A30','Samsung','Cinza','','','Caixa 1 - Samsung',15,1,NULL),(247,'Capinha','Galaxy A01 Core','Samsung','Preto','','','Caixa 1 - Samsung',15,1,NULL),(248,'Capinha','Galaxy A22 5G','Samsung','Preto','','','Mostruário',20,1,NULL),(250,'Capinha','Galaxy J5 Pro','Samsung','Preto','','','Caixa 1 - Samsung',15,2,NULL),(251,'Capinha','Galaxy A51','Samsung','Preto','','','Caixa 1 - Samsung',15,1,NULL),(252,'Capinha','Galaxy A51','Samsung','Dourado','','','Caixa 1 - Samsung',15,1,NULL),(253,'Capinha','Galaxy A21','Samsung','Preto','','','Caixa 1 - Samsung',20,1,NULL),(254,'Capinha','Galaxy A52','Samsung','Preto','','','Mostruário',15,2,NULL),(255,'Capinha','Galaxy J4 Plus','Samsung','Preto','','','Caixa 1 - Samsung',15,2,NULL),(257,'Capinha','Galaxy J4 Plus','Samsung','Rosa','','','Caixa 1 - Samsung',15,1,NULL),(258,'Capinha','Galaxy J6','Samsung','Rosa','','','Caixa 1 - Samsung',15,1,NULL),(260,'Capinha','Galaxy A14 ','Samsung','Vinho','','','Caixa 1 - Samsung',20,1,NULL),(261,'Capinha','Galaxy A14 ','Samsung','Preto','','','Caixa 1 - Samsung',20,1,NULL),(263,'Capinha','Galaxy A03s','Samsung','Preto','','','Caixa 1 - Samsung',20,1,NULL),(264,'Capinha','Galaxy A03s','Samsung','Rosa','','Rosa com detalhes.','Caixa 1 - Samsung',20,1,NULL),(265,'Capinha','Galaxy A10s','Samsung','Rosa','','','Caixa 1 - Samsung',20,0,NULL),(266,'Capinha','Galaxy A30s/A50s/A50','Samsung','Transparente','','','Caixa 1 - Samsung',15,1,NULL),(267,'Capinha','Galaxy A30s/A50s/A50','Samsung','Vermelho','','','Caixa 1 - Samsung',15,1,NULL),(270,'Capinha','Galaxy S21','Samsung','Transparente','','','Caixa 1 - Samsung',20,3,NULL),(271,'Capinha','Galaxy S21 Plus','Samsung','Transparente','','','Caixa 1 - Samsung',20,2,NULL),(274,'Capinha','Galaxy A13','Samsung','Rosa','','','Caixa 1 - Samsung',20,0,NULL),(275,'Capinha','Galaxy A22 4G','Samsung','Colorido','','','Caixa 1 - Samsung',20,1,NULL),(277,'Capinha','Galaxy A51','Samsung','Laranja','','','Mostruário',20,1,NULL),(278,'Capinha','Galaxy A51','Samsung','Amarelo','','','Mostruário',20,1,NULL),(279,'Capinha','Galaxy M32','Samsung','Roxo','','','Caixa 1 - Samsung',20,1,NULL),(280,'Capinha','Galaxy A22 5G','Samsung','Rosa','','','Mostruário',20,1,NULL),(281,'Capinha','Galaxy S20','Samsung','Vinho','','','Mostruário',20,1,NULL),(282,'Capinha','Note 9','Xiaomi Redmi','Roxo','','','Mostruário',15,1,NULL),(283,'Capinha','Galaxy A10','Samsung','Preto','','','Mostruário',20,2,NULL),(284,'Capinha','Galaxy A71','Samsung','Rosa','','','Caixa 1 - Samsung',20,1,NULL),(285,'Capinha','Galaxy A02','Samsung','Rosa','','','Caixa 1 - Samsung',20,2,NULL),(286,'Capinha','Note 9','Xiaomi Redmi','Azul','','','Mostruário',15,1,NULL),(287,'Capinha','Galaxy S20','Samsung','Laranja','','','Mostruário',20,2,NULL),(288,'Capinha','Galaxy A51','Samsung','Rosa','','','Mostruário',20,1,NULL),(289,'Capinha','Galaxy J7 Pro','Samsung','Rosa','','','Caixa 1 - Samsung',15,1,NULL),(290,'Capinha','Galaxy M32','Samsung','Verde Água','','','Caixa 1 - Samsung',20,1,NULL),(291,'Capinha','Galaxy S20','Samsung','Lilás','','','Mostruário',20,1,NULL),(292,'Capinha','Galaxy M32','Samsung','Rosa','','','Caixa 1 - Samsung',20,1,NULL),(293,'Capinha','Note 9s','Xiaomi Redmi','Rosa','','','Mostruário',15,1,NULL),(294,'Capinha','Note 9','Xiaomi Redmi','Verde','','','Mostruário',15,1,NULL),(295,'Capinha','Note 9s','Xiaomi Redmi','Rosa Claro','','','Mostruário',15,1,NULL),(296,'Capinha','Note 8','Xiaomi Redmi','Vinho','','','Mostruário',15,1,NULL),(298,'Capinha','Note 8','Xiaomi Redmi','Preto','','','Mostruário',15,1,NULL),(299,'Capinha','Mi 8 Lite','Xiaomi','Vermelho','','','Caixa 1 - Xiaomi',15,1,NULL),(300,'Capinha','Mi 8','Xiaomi','Vermelho','','','Caixa 1 - Xiaomi',15,1,NULL),(301,'Capinha','Galaxy J7 Prime','Samsung','Rosa','','','Caixa 1 - Samsung',15,1,NULL),(302,'Capinha','Note 9','Xiaomi Redmi','Preto','','','Caixa 1 - Xiaomi',15,1,NULL),(303,'Capinha','Mi 8','Xiaomi','Cinza','','','Caixa 1 - Xiaomi',15,1,NULL),(305,'Capinha','Mi 8','Xiaomi','Preto','','','Caixa 1 - Xiaomi',15,1,NULL),(306,'Capinha','Galaxy S9','Samsung','Cinza','','','Caixa 1 - Samsung',15,1,NULL),(307,'Capinha','Note 8','Xiaomi Redmi','Azul Escuro','','','Mostruário',15,1,NULL),(308,'Capinha','Galaxy S9 Plus','Samsung','Transparente','','','Caixa 1 - Samsung',15,2,NULL),(309,'Capinha','Galaxy A70','Samsung','Cinza','','','Caixa 1 - Samsung',15,1,NULL),(310,'Capinha','Galaxy A70','Samsung','Dourada','','','Caixa 1 - Samsung',15,1,NULL),(311,'Capinha','Galaxy A32','Samsung','Transparente','','','Caixa 1 - Samsung',20,1,NULL),(312,'Capinha','Galaxy A71','Samsung','Vermelho','','','Caixa 1 - Samsung',20,1,NULL),(313,'Capinha','Galaxy S10e','Samsung','Branco','','','Caixa 1 - Samsung',15,1,NULL),(314,'Capinha','Galaxy A72','Samsung','Transparente','','','Caixa 1 - Samsung',15,3,NULL),(315,'Capinha','Galaxy A80','Samsung','Preto','','','Caixa 1 - Samsung',20,2,NULL),(316,'Capinha','Galaxy A11','Samsung','Rosa','','','Mostruário',20,2,NULL),(317,'Capinha','Galaxy S10 Plus','Samsung','Dourado','','','Caixa 1 - Samsung',15,1,NULL),(319,'Capinha','Galaxy S10','Samsung','Rosa','','','Caixa 1 - Samsung',15,1,NULL),(322,'Capinha','Galaxy A30s/A50s/A50','Samsung','Cinza','','','Caixa 1 - Samsung',15,2,NULL),(323,'Capinha','Galaxy A13','Samsung','Vermelho','','','Caixa 1 - Samsung',20,1,NULL),(324,'Capinha','Note 11/11S','Xiaomi Redmi','Verde','','','Mostruário',20,1,NULL),(325,'Capinha','Galaxy M62','Samsung','Preto','','','Caixa 1 - Samsung',20,1,NULL),(326,'Capinha','Galaxy A52','Samsung','Branco','','','Mostruário',15,1,NULL),(327,'Capinha','Galaxy S21 Plus','Samsung','Vermelho','','','Caixa 1 - Samsung',20,1,NULL),(328,'Capinha','Galaxy S10 Plus','Samsung','Cinza','','','Caixa 1 - Samsung',15,1,NULL),(329,'Capinha','Galaxy A12','Samsung','Rosa','','','Caixa 1 - Samsung',20,2,NULL),(330,'Capinha','Galaxy A12','Samsung','Lilás','','','Caixa 1 - Samsung',20,1,NULL),(331,'Capinha','Galaxy A02s','Samsung','Rosa','','','Mostruário',20,2,NULL),(332,'Capinha','Galaxy A30','Samsung','Preto','','','Caixa 1 - Samsung',15,1,NULL),(333,'Capinha','Galaxy A51','Samsung','Lilás','','','Mostruário',20,1,NULL),(335,'Capinha','Note 11/11S','Xiaomi Redmi','Verde Água','','','Mostruário',20,1,NULL),(336,'Capinha','M3','Xiaomi Pocophone','Verde','','','Mostruário',20,1,NULL),(337,'Capinha','Galaxy A20s','Samsung','Preto','','','Caixa 1 - Samsung',15,2,NULL),(338,'Capinha','Galaxy A20s','Samsung','Azul','','','Caixa 1 - Samsung',20,1,NULL),(339,'Capinha','Galaxy A20s','Samsung','Vermelho','','','Caixa 1 - Samsung',20,1,NULL),(340,'Capinha','Galaxy A31','Samsung','Vermelho','','','Caixa 1 - Samsung',20,1,NULL),(341,'Capinha','Galaxy A02s','Samsung','Verde','','','Mostruário',20,1,NULL),(343,'Capinha','Galaxy A30s/A50s/A50','Samsung','Verde Água','','','Caixa 1 - Samsung',20,1,NULL),(344,'Capinha','Galaxy A32','Samsung','Verde','','','Mostruário',20,1,NULL),(345,'Capinha','Galaxy A02s','Samsung','Branco','','','Mostruário',20,1,NULL),(346,'Capinha','Note 11/11S','Xiaomi Redmi','Laranja','','','Mostruário',20,1,NULL),(347,'Capinha','Note 11/11S','Xiaomi Redmi','Preto','','','Mostruário',20,1,NULL),(348,'Capinha','Galaxy A02s','Samsung','Lilás','','','Mostruário',20,1,NULL),(349,'Capinha','Mi 9','Xiaomi','Cinza','','','Caixa 1 - Xiaomi',15,1,NULL),(350,'Capinha','Galaxy A10','Samsung','Azul','','','Mostruário',20,1,NULL),(351,'Capinha','Galaxy A11','Samsung','Rosa','','','Mostruário',15,1,NULL),(352,'Capinha','Galaxy A52','Samsung','Verde','','','Mostruário',15,1,NULL),(353,'Capinha','Galaxy A52','Samsung','Rosa','','','Mostruário',15,1,NULL),(354,'Capinha','Galaxy A03s','Samsung','Lilás','','','Caixa 1 - Samsung',20,1,NULL),(355,'Capinha','Galaxy A20','Samsung','Cinza','','','Caixa 1 - Samsung',15,2,NULL),(356,'Capinha','Galaxy A03s','Samsung','Rosa','','','Caixa 1 - Samsung',20,1,NULL),(357,'Capinha','Galaxy M62','Samsung','Verde Água','','','Caixa 1 - Samsung',20,1,NULL),(358,'Capinha','Galaxy A03','Samsung','Rosa','','','Caixa 1 - Samsung',20,1,NULL),(359,'Capinha','Galaxy J7 Prime','Samsung','Lilás','','','Caixa 1 - Samsung',20,1,NULL),(361,'Capinha','Moto G60s','Motorola','Vermelho','','','Mostruário',20,1,NULL),(362,'Capinha','Moto G8','Motorola','Preto','','','Caixa 1 - Motorola',20,4,NULL),(364,'Capinha','Moto G22','Motorola','Preto','','','Caixa 1 - Motorola',20,1,NULL),(365,'Capinha','Moto G22','Motorola','Azul','','','Mostruário',20,1,NULL),(366,'Capinha','Moto G9 Plus','Motorola','Preto','','','Mostruário',20,1,NULL),(367,'Capinha','Moto One Vision','Motorola','Azul','','','Mostruário',20,1,NULL),(368,'Capinha','Moto E20','Motorola','Branco','','','Caixa 1 - Motorola',20,1,NULL),(369,'Capinha','Moto G10/G20/G30','Motorola','Preto','','','Caixa 1 - Motorola',20,1,NULL),(370,'Capinha','Moto G8 Power','Motorola','Preto','','','Caixa 1 - Motorola',20,1,NULL),(371,'Capinha','Moto One Vision','Motorola','Vinho','','','Mostruário',20,1,NULL),(372,'Capinha','Moto One Vision','Motorola','Rosa','','','Mostruário',20,1,NULL),(373,'Capinha','Moto One Vision','Motorola','Vermelho','','','Mostruário',20,1,NULL),(374,'Capinha','Moto G9 Play','Motorola','Preto','','','Mostruário',20,2,NULL),(375,'Capinha','Moto G10/G20/G30','Motorola','Laranja','','','Mostruário',20,1,NULL),(376,'Capinha','Moto G10/G20/G30','Motorola','Vermelho','','','Mostruário',20,1,NULL),(377,'Capinha','Moto G10/G20/G30','Motorola','Branco','','','Mostruário',20,1,NULL),(378,'Capinha','Moto E7 Plus','Motorola','Transparente','','','Caixa 1 - Motorola',15,1,NULL),(379,'Capinha','Moto G9 Plus','Motorola','Transparente','','','Caixa 1 - Motorola',15,1,NULL),(380,'Capinha','Moto G8 Power','Motorola','Verde','','','Caixa 1 - Motorola',20,1,NULL),(381,'Capinha','Moto G22','Motorola','Vermelho','','','Mostruário',20,1,NULL),(382,'Capinha','Moto E7','Motorola','Verde Água','','','Mostruário',20,1,NULL),(383,'Capinha','Moto G9 Plus','Motorola','Vermelho','','','Mostruário',20,1,NULL),(384,'Capinha','Moto G22','Motorola','Lilás','','','Mostruário',20,1,NULL),(385,'Capinha','Moto G9 Plus','Motorola','Verde','','','Mostruário',20,1,NULL),(386,'Capinha','Galaxy S20','Samsung','Amarelo','','','Mostruário',20,1,NULL),(387,'Capinha','Galaxy A21s','Samsung','Preto','','','Caixa 1 - Samsung',20,1,NULL),(388,'Capinha','Galaxy A14','Samsung','Lilás','','','Caixa 1 - Samsung',20,1,NULL),(391,'Capinha','Galaxy J7 Pro','Samsung','Dourado','','','Caixa 1 - Samsung',15,2,NULL),(392,'Capinha','Galaxy J7 Pro','Samsung','Cinza','','','Caixa 1 - Samsung',15,1,NULL),(393,'Capinha','Galaxy A52','Samsung','Amarelo','','','Mostruário',15,1,NULL),(394,'Capinha','Galaxy A31','Samsung','Preto','','','Caixa 1 - Samsung',20,1,NULL),(395,'Capinha','Galaxy A21','Samsung','Rosa','','','Caixa 1 - Samsung',20,1,NULL),(397,'Capinha','Moto G40 Fusion/G60','Motorola','Rosa','','','Mostruário',20,1,NULL),(398,'Capinha','Moto G40 Fusion/G60','Motorola','Azul Bebê','','','Mostruário',20,1,NULL),(399,'Capinha','Moto G10/G20/G30','Motorola','Verde','','','Caixa 1 - Motorola',20,1,NULL),(400,'Capinha','Moto G8 Plus','Motorola','Azul','','','Mostruário',20,1,NULL),(401,'Capinha','Moto G8 Plus','Motorola','Lilás','','','Mostruário',20,1,NULL),(402,'Capinha','Moto G8 Plus','Motorola','Rosa','','','Mostruário',20,1,NULL),(403,'Capinha','Galaxy S21 Ultra','Samsung','Azul','','','Caixa 1 - Samsung',20,1,NULL),(404,'Capinha','Galaxy S21 Ultra','Samsung','Rosa','','','Caixa 1 - Samsung',20,2,NULL),(405,'Capinha','iPhone 14','Apple','Rosa','','','Caixa 1 - iPhone',30,1,NULL),(406,'Capinha','iPhone 14','Apple','Azul','','','Caixa 1 - iPhone',30,1,NULL),(407,'Capinha','iPhone 14 Pro','Apple','Rosa','','','Caixa 1 - iPhone',30,2,NULL),(408,'Capinha','iPhone 14 Pro','Apple','Azul','','','Caixa 1 - iPhone',30,1,NULL),(409,'Capinha','iPhone 12','Apple','Vermelho','','','Mostruário',30,1,NULL),(410,'Capinha','iPhone 12','Apple','Preto','','','Mostruário',30,1,NULL),(411,'Capinha','iPhone 12','Apple','Verde','','','Mostruário',30,1,NULL),(412,'Capinha','iPhone 13 Mini','Apple','Lilás','','','Caixa 1 - iPhone',30,1,NULL),(413,'Capinha','iPhone 13 Mini','Apple','Rosa','','','Caixa 1 - iPhone',30,1,NULL),(414,'Capinha','iPhone 13 Mini','Apple','Preto','','','Caixa 1 - iPhone',30,1,NULL),(415,'Capinha','iPhone 13 Pro Max','Apple','Rosa','','','Mostruário',30,1,NULL),(416,'Capinha','iPhone 13 Pro Max','Apple','Preto','','','Mostruário',30,1,NULL),(417,'Capinha','iPhone 12 Pro','Apple','Bege','','','Mostruário',30,1,NULL),(418,'Capinha','iPhone 12 Pro','Apple','Azul','','','Mostruário',30,1,NULL),(419,'Capinha','iPhone 12 Pro','Apple','Cinza','','','Mostruário',30,1,NULL),(420,'Capinha','iPhone 12 Pro','Apple','Roxo','','','Mostruário',30,1,NULL),(421,'Capinha','iPhone 12 Pro','Apple','Verde','','','Mostruário',30,1,NULL),(422,'Capinha','iPhone 12 Pro','Apple','Branco','','','Mostruário',30,1,NULL),(423,'Capinha','iPhone 12 Pro','Apple','Marrom','','','Mostruário',30,1,NULL),(424,'Capinha','iPhone 12 Pro','Apple','Rosa','','','Mostruário',30,1,NULL),(425,'Capinha','iPhone XS Max','Apple','Transparente','','','Caixa 1 - iPhone',20,1,NULL),(426,'Capinha','iPhone XS Max','Apple','Vermelho','','','Mostruário',20,1,NULL),(427,'Capinha','iPhone XS Max','Apple','Azul','','','Mostruário',20,1,NULL),(428,'Capinha','iPhone XS Max','Apple','Preto','','','Mostruário',20,1,NULL),(429,'Capinha','iPhone XS Max','Apple','Bege','','','Mostruário',20,1,NULL),(430,'Capinha','iPhone 7 Plus/8 Plus','Apple','Preto','','','Mostruário',20,2,NULL),(431,'Capinha','iPhone 7 Plus/8 Plus','Apple','Rosa','','','Mostruário',20,3,NULL),(432,'Capinha','iPhone 7 Plus/8 Plus','Apple','Azul','','','Mostruário',20,4,NULL),(433,'Capinha','iPhone 7 Plus/8 Plus','Apple','Verde Água','','','Mostruário',20,1,NULL),(436,'Capinha','iPhone 11 Pro','Apple','Azul','','','Caixa 1 - iPhone',30,1,NULL),(437,'Capinha','iPhone 11 Pro','Apple','Transparente','','','Caixa 1 - iPhone',30,3,NULL),(438,'Capinha','iPhone 11 Pro','Apple','Verde','','','Caixa 1 - iPhone',30,1,NULL),(439,'Capinha','iPhone 11 Pro','Apple','Vermelho','','','Mostruário',30,1,NULL),(440,'Capinha','iPhone 11 Pro','Apple','Preto','','','Mostruário',30,1,NULL),(441,'Capinha','iPhone 11 Pro','Apple','Rosa','','','Mostruário',30,1,NULL),(442,'Capinha','iPhone 12 Pro Max','Apple','Branco','','','Mostruário',30,1,NULL),(443,'Capinha','iPhone 12 Pro Max','Apple','Cinza','','','Mostruário',30,1,NULL),(444,'Capinha','iPhone 12 Pro Max','Apple','Azul','','','Mostruário',30,2,NULL),(445,'Capinha','iPhone 12 Pro Max','Apple','Bege','','','Mostruário',30,1,NULL),(446,'Capinha','iPhone 12 Pro Max','Apple','Preto','','','Mostruário',30,1,NULL),(447,'Capinha','iPhone 12 Pro Max','Apple','Verde','','','Mostruário',30,1,NULL),(448,'Capinha','iPhone 12 Pro Max','Apple','Vermelho','','','Mostruário',30,1,NULL),(449,'Capinha','iPhone 14 Plus','Apple','Azul','','','Mostruário',30,1,NULL),(450,'Capinha','iPhone 14 Plus','Apple','Vermelho','','','Mostruário',30,1,NULL),(451,'Capinha','iPhone 14 Plus','Apple','Lilás','','','Mostruário',30,1,NULL),(452,'Capinha','iPhone 13 Pro','Apple','Rosa','','','Caixa 1 - iPhone',30,1,NULL),(453,'Capinha','iPhone 7/8','Apple','Verde','','','Caixa 1 - iPhone',20,1,NULL),(454,'Capinha','iPhone 7/8','Apple','Rosa','','','Caixa 1 - iPhone',20,2,NULL),(455,'Capinha','iPhone 7/8','Apple','Vermelho','','','Caixa 1 - iPhone',20,1,NULL),(456,'Capinha','iPhone 13','Apple','Vinho','','','Mostruário',30,1,NULL),(457,'Capinha','iPhone X/XS','Apple','Branco','','','Mostruário',20,1,NULL),(458,'Capinha','iPhone X/XS','Apple','Rosa','','','Mostruário',20,3,NULL),(459,'Capinha','iPhone X/XS','Apple','Bege','','','Mostruário',20,1,NULL),(460,'Capinha','iPhone X/XS','Apple','Azul','','','Mostruário',20,1,NULL),(461,'Capinha','iPhone 7/8','Apple','Lilás','','Colorido.','Caixa 1 - iPhone',20,1,NULL),(462,'Capinha','iPhone 7/8','Apple','Transparente','','Fosco com lilás nas bordas.','Caixa 1 - iPhone',20,1,NULL),(463,'Capinha','iPhone XR','Apple','Azul','','','Mostruário',20,1,NULL),(464,'Capinha','iPhone XR','Apple','Roxo','','','Mostruário',20,1,NULL),(465,'Capinha','iPhone XR','Apple','Rosa','','','Mostruário',20,1,NULL),(466,'Capinha','iPhone 6 Plus/6s Plus','Apple','Roxo','','','Caixa 1 - iPhone',20,2,NULL),(467,'Capinha','iPhone 6 Plus/6s Plus','Apple','Laranja','','','Caixa 1 - iPhone',20,1,NULL),(468,'Capinha','iPhone 6 Plus/6s Plus','Apple','Cinza','','','Caixa 1 - iPhone',20,1,NULL),(469,'Capinha','iPhone 14 Pro Max','Apple','Vermelho','','','Mostruário',30,1,NULL),(470,'Capinha','iPhone 14 Pro Max','Apple','Verde','','','Mostruário',30,1,NULL),(471,'Capinha','iPhone 14 Pro Max','Apple','Azul','','','Mostruário',30,1,NULL),(472,'Capinha','iPhone 6/6s','Apple','Azul','','','Caixa 1 - iPhone',20,1,NULL),(473,'Capinha','iPhone 11 Pro Max','Apple','Preto','','','Caixa 1 - iPhone',30,1,NULL),(474,'Capinha','iPhone 11 Pro Max','Apple','Transparente','','','Caixa 1 - iPhone',30,2,NULL),(475,'Capinha','iPhone 11','Apple','Transparente','','','Caixa 1 - iPhone',30,2,NULL),(476,'Capinha','iPhone 11','Apple','Rosa','','','Caixa 1 - iPhone',30,2,NULL),(477,'Capinha','iPhone 11','Apple','Roxo','','','Caixa 1 - iPhone',30,1,NULL),(478,'Película','Moto E6 Plus','Motorola','','Vidro','','Caixa 3',10,3,NULL),(479,'Película','Moto E5/G6 Play','Motorola','','Vidro','','Caixa 3',10,9,NULL),(480,'Película','One Vision','Motorola','','Vidro','','Caixa 3',10,4,NULL),(481,'Película','Moto Z3 Play','Motorola','','Vidro','','Caixa 3',10,4,NULL),(482,'Película','Moto G/G2','Motorola','','Vidro','','Caixa 3',10,2,NULL),(483,'Película','One Zoom','Motorola','Preto','Vidro','','Caixa 3',20,3,NULL),(484,'Película','Moto Z3','Motorola','','Vidro','','Caixa 3',10,3,NULL),(485,'Película','Moto Z3 Play','Motorola','Branco','Plástico','','Caixa 3',10,2,NULL),(486,'Película','Moto G3','Motorola','','Vidro','','Caixa 3',10,4,NULL),(487,'Película','Moto E7 Plus','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(488,'Película','Moto G5S Plus','Motorola','Branco','Vidro','','Caixa 3',20,1,NULL),(489,'Película','Moto Z Play','Motorola','','Vidro','','Caixa 3',10,7,NULL),(490,'Película','One','Motorola','Preto','Vidro','','Caixa 3',20,4,NULL),(491,'Película','Moto G6','Motorola','Preto','Plástico','','Caixa 3',10,1,NULL),(493,'Película','Moto E30','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(494,'Película','Moto G9 Plus','Motorola','Preto','Vidro','','Caixa 3',20,6,NULL),(495,'Película','Moto E7','Motorola','Preto','Vidro','','Caixa 3',20,5,NULL),(496,'Película','Moto G7 Play','Motorola','','Vidro','','Caixa 3',10,6,NULL),(497,'Película','Moto G7 Play','Motorola','Preto','Vidro','','Caixa 3',20,7,NULL),(498,'Película','Moto E4 Plus','Motorola','','Vidro','','Caixa 3',10,6,NULL),(499,'Película','Moto G60','Motorola','Preto','Vidro','','Caixa 3',20,5,NULL),(500,'Película','Moto G40','Motorola','Preto','Vidro','','Caixa 3',20,4,NULL),(501,'Película','Moto G 5G','Motorola','Preto','Vidro','','Caixa 3',20,2,NULL),(502,'Película','Moto G6','Motorola','Preto','Vidro','','Caixa 3',20,5,NULL),(503,'Película','Moto E6','Motorola','Preto','Vidro','','Caixa 3',20,3,NULL),(504,'Película','One Hyper','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(505,'Película','One Fusion Plus','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(506,'Película','Moto E6 Plus','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(507,'Película','Moto E5/G6 Play','Motorola','Preto','Vidro','','Caixa 3',20,7,NULL),(508,'Película','Moto G51','Motorola','Preto','Vidro','','Caixa 3',20,6,NULL),(509,'Película','Moto X1','Motorola','','Vidro','','Caixa 3',10,5,NULL),(510,'Película','Moto C','Motorola','','Vidro','','Caixa 3',10,6,NULL),(511,'Película','Moto E6 Play','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(512,'Película','Moto E5 Plus','Motorola','','Vidro','','Caixa 3',10,15,NULL),(513,'Película','One Vision','Motorola','Preto','Vidro','','Caixa 3',20,5,NULL),(514,'Película','Moto C Plus','Motorola','','Vidro','','Caixa 3',10,5,NULL),(515,'Película','Moto G7 Power','Motorola','','Vidro','','Caixa 3',10,10,NULL),(516,'Película','Moto G7 Power','Motorola','Preto','Vidro','','Caixa 3',20,4,NULL),(517,'Película','Moto G6 Plus','Motorola','','Vidro','','Caixa 3',10,5,NULL),(518,'Película','Moto G7/G7 Plus','Motorola','Preto','Vidro','','Caixa 3',20,11,NULL),(519,'Película','Moto X Play','Motorola','','Vidro','','Caixa 3',10,3,NULL),(520,'Película','Moto G8 Plus','Motorola','Preto','Vidro','','Caixa 3',20,3,NULL),(521,'Película','Moto G8 Power','Motorola','Preto','Vidro','','Caixa 3',20,4,NULL),(522,'Película','Moto G8 Play','Motorola','','Vidro','','Caixa 3',10,13,NULL),(523,'Película','Moto G8 Play','Motorola','Preto','Vidro','','Caixa 3',20,1,NULL),(524,'Película','G8X ThinQ','LG','Preto','Vidro','','Caixa 2',20,2,NULL),(525,'Película','Moto G8 Power Lite','Motorola','Preto','Vidro','','Caixa 3',20,3,NULL),(526,'Película','Moto G5','Motorola','','Vidro','','Caixa 3',10,13,NULL),(527,'Película','Moto G4','Motorola','','Vidro','','Caixa 3',10,3,NULL),(528,'Película','Galaxy M62','Samsung','Preto','Vidro','','Caixa 1',20,1,NULL),(529,'Capinha','Note 9','Xiaomi Redmi','Azul','','','Caixa 1 - Xiaomi',15,1,NULL),(530,'Capinha','Galaxy A51','Samsung','Amarelo','','','Caixa 1 - Samsung',20,1,NULL),(531,'Capinha','Galaxy A30s/A50s/A50','Samsung','Vermelho','','','Caixa 1 - Samsung',20,1,NULL);
/*!40000 ALTER TABLE `estoque` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicos`
--

DROP TABLE IF EXISTS `servicos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicos` (
  `idSer` int NOT NULL AUTO_INCREMENT,
  `idEst` int DEFAULT NULL,
  `idTipSer` int NOT NULL,
  `codigoSer` varchar(6) DEFAULT NULL,
  `dataSer` date DEFAULT NULL,
  `precoSer` double DEFAULT NULL,
  `detalhesSer` varchar(255) DEFAULT NULL,
  `quantidadeSer` int DEFAULT NULL,
  PRIMARY KEY (`idSer`),
  KEY `fk_servicos_estoque1_idx` (`idEst`),
  KEY `fk_servicos_tiposervico1_idx` (`idTipSer`),
  CONSTRAINT `fk_servicos_estoque1` FOREIGN KEY (`idEst`) REFERENCES `estoque` (`idEst`),
  CONSTRAINT `fk_servicos_tiposervico1` FOREIGN KEY (`idTipSer`) REFERENCES `tiposervico` (`idTipSer`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicos`
--

LOCK TABLES `servicos` WRITE;
/*!40000 ALTER TABLE `servicos` DISABLE KEYS */;
/*!40000 ALTER TABLE `servicos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tiposervico`
--

DROP TABLE IF EXISTS `tiposervico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tiposervico` (
  `idTipSer` int NOT NULL AUTO_INCREMENT,
  `descricaoTipSer` varchar(255) DEFAULT NULL,
  `areaTipSer` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idTipSer`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tiposervico`
--

LOCK TABLES `tiposervico` WRITE;
/*!40000 ALTER TABLE `tiposervico` DISABLE KEYS */;
/*!40000 ALTER TABLE `tiposervico` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-17  2:39:55