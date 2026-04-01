-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: snch_billetterie
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `arret`
--

DROP TABLE IF EXISTS `arret`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `arret` (
  `id_arret` int NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_arret`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `arret`
--
-- ORDER BY:  `id_arret`

LOCK TABLES `arret` WRITE;
/*!40000 ALTER TABLE `arret` DISABLE KEYS */;
/*!40000 ALTER TABLE `arret` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `itineraire`
--

DROP TABLE IF EXISTS `itineraire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `itineraire` (
  `id_itineraire` int NOT NULL AUTO_INCREMENT,
  `duree_prevue` int NOT NULL,
  PRIMARY KEY (`id_itineraire`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `itineraire`
--
-- ORDER BY:  `id_itineraire`

LOCK TABLES `itineraire` WRITE;
/*!40000 ALTER TABLE `itineraire` DISABLE KEYS */;
INSERT INTO `itineraire` (`id_itineraire`, `duree_prevue`) VALUES (3,111),(5,10),(6,111),(8,111),(9,30),(10,51646),(11,567);
/*!40000 ALTER TABLE `itineraire` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `train`
--

DROP TABLE IF EXISTS `train`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `train` (
  `id_train` int NOT NULL AUTO_INCREMENT,
  `numero_train` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type_train` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id_train`),
  UNIQUE KEY `numero_train` (`numero_train`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `train`
--
-- ORDER BY:  `id_train`

LOCK TABLES `train` WRITE;
/*!40000 ALTER TABLE `train` DISABLE KEYS */;
INSERT INTO `train` (`id_train`, `numero_train`, `type_train`) VALUES (3,'5665','TER');
/*!40000 ALTER TABLE `train` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trajet`
--

DROP TABLE IF EXISTS `trajet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trajet` (
  `id_trajet` int NOT NULL AUTO_INCREMENT,
  `id_train` int NOT NULL,
  `id_itineraire` int NOT NULL,
  `id_arret_depart` int NOT NULL,
  `id_arret_arrive` int NOT NULL,
  `date_heure_depart` datetime NOT NULL,
  `date_heure_arrivee` datetime NOT NULL,
  PRIMARY KEY (`id_trajet`),
  KEY `fk_trajet_itineraire` (`id_itineraire`),
  KEY `fk_trajet_train` (`id_train`),
  KEY `fk_trajet_arret_depart` (`id_arret_depart`),
  KEY `fk_trajet_arret_arrive` (`id_arret_arrive`),
  CONSTRAINT `fk_trajet_arret_arrive` FOREIGN KEY (`id_arret_arrive`) REFERENCES `arret` (`id_arret`),
  CONSTRAINT `fk_trajet_arret_depart` FOREIGN KEY (`id_arret_depart`) REFERENCES `arret` (`id_arret`),
  CONSTRAINT `fk_trajet_itineraire` FOREIGN KEY (`id_itineraire`) REFERENCES `itineraire` (`id_itineraire`),
  CONSTRAINT `fk_trajet_train` FOREIGN KEY (`id_train`) REFERENCES `train` (`id_train`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trajet`
--
-- ORDER BY:  `id_trajet`

LOCK TABLES `trajet` WRITE;
/*!40000 ALTER TABLE `trajet` DISABLE KEYS */;
/*!40000 ALTER TABLE `trajet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utilisateur`
--

DROP TABLE IF EXISTS `utilisateur`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utilisateur` (
  `id_utilisateur` int NOT NULL AUTO_INCREMENT,
  `login` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `mot_de_passe` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nom` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `prenom` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` enum('PLANIFICATEUR','ADMIN') COLLATE utf8mb4_unicode_ci NOT NULL,
  `actif` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_utilisateur`),
  UNIQUE KEY `login` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utilisateur`
--
-- ORDER BY:  `id_utilisateur`

LOCK TABLES `utilisateur` WRITE;
/*!40000 ALTER TABLE `utilisateur` DISABLE KEYS */;
INSERT INTO `utilisateur` (`id_utilisateur`, `login`, `mot_de_passe`, `nom`, `prenom`, `email`, `role`, `actif`, `created_at`) VALUES (1,'agent1','1234','Durand','Alice',NULL,'PLANIFICATEUR',1,'2026-02-05 18:08:29'),(2,'planif1','1234','Martin','Lucas',NULL,'PLANIFICATEUR',1,'2026-02-05 18:08:29'),(3,'admin1','1234','Admin','SNCH',NULL,'ADMIN',1,'2026-02-05 19:32:02');
/*!40000 ALTER TABLE `utilisateur` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-01 16:22:16
