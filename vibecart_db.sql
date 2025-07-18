-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: vibecart_db
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
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `otp` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `verified` bit(1) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1j9d9a06i600gd43uu3km82jw` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES (14,'RAJ24KUSH@GMAIL.COM','raj',NULL,'$2a$10$ro3MTZS7Tg6cTb5gS9CYvu5rPUdK4Vh6Ylb4SHn3n026poL.PvMUG','CUSTOMER',_binary '','6264100725');
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item`
--

DROP TABLE IF EXISTS `cart_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `product_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `product_price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn1boxy63txpdcfv6ahrw2wdui` (`user_id`),
  KEY `FK_product_cartitem` (`product_id`),
  CONSTRAINT `FK_product_cartitem` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKn1boxy63txpdcfv6ahrw2wdui` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item`
--

LOCK TABLES `cart_item` WRITE;
/*!40000 ALTER TABLE `cart_item` DISABLE KEYS */;
INSERT INTO `cart_item` VALUES (6,9,4348,14,'/images/product2.jpeg','Product 2',1002),(8,6,4347,14,'/images/product1.jpeg','Product 1',1001),(9,1,4349,14,'/images/product3.jpeg','Product 3',1003);
/*!40000 ALTER TABLE `cart_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart_item_seq`
--

DROP TABLE IF EXISTS `cart_item_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_item_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_item_seq`
--

LOCK TABLES `cart_item_seq` WRITE;
/*!40000 ALTER TABLE `cart_item_seq` DISABLE KEYS */;
INSERT INTO `cart_item_seq` VALUES (1);
/*!40000 ALTER TABLE `cart_item_seq` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price` double NOT NULL,
  `quantity` int NOT NULL,
  `total_price` double NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `product_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt4dc2r9nbvbujrljv3e23iibt` (`order_id`),
  KEY `FK551losx9j75ss5d6bfsqvijna` (`product_id`),
  CONSTRAINT `FK551losx9j75ss5d6bfsqvijna` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKt4dc2r9nbvbujrljv3e23iibt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1001,6,6006,1,4347),(2,1003,1,1003,2,4349),(3,1003,1,1003,3,4349);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `delivery_address` varchar(255) DEFAULT NULL,
  `order_date` datetime(6) DEFAULT NULL,
  `order_status` varchar(255) DEFAULT NULL,
  `payment_method` varchar(255) DEFAULT NULL,
  `total_amount` double NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKosotb7rvgk4myda45ki0cf9j` (`user_id`),
  CONSTRAINT `FKosotb7rvgk4myda45ki0cf9j` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'b-58, balaji, patelnagar, Bhopal, Madhya Pradesh - 462022','2025-07-19 02:57:19.125526','PLACED','COD',0,14),(2,'b-58, balaji, patelnagar, Bhopal, Madhya Pradesh - 462022','2025-07-19 02:58:30.904804','PLACED','COD',0,14),(3,'b-58, balaji, patelnagar, Bhopal, Madhya Pradesh - 462022','2025-07-19 03:01:53.958334','PLACED','COD',0,14);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `stock` int NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `seller_id` bigint DEFAULT NULL,
  `seller_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKthco7cl7o18ewrdcav6n9llbe` (`seller_id`),
  CONSTRAINT `FKthco7cl7o18ewrdcav6n9llbe` FOREIGN KEY (`seller_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4350 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (4347,'Demo product 1','/images/product1.jpeg','Product 1',1001,10,'Demo',NULL,NULL,NULL),(4348,'Demo product 2','/images/product2.jpeg','Product 2',1002,10,'Demo',NULL,NULL,NULL),(4349,'Demo product 3','/images/product3.jpeg','Product 3',1003,10,'Demo',NULL,NULL,NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_seq`
--

DROP TABLE IF EXISTS `user_seq`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_seq`
--

LOCK TABLES `user_seq` WRITE;
/*!40000 ALTER TABLE `user_seq` DISABLE KEYS */;
INSERT INTO `user_seq` VALUES (1);
/*!40000 ALTER TABLE `user_seq` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-19  3:28:12
