DROP DATABASE IF EXISTS user;
CREATE DATABASE user;
USE user;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user_credential_tb;
DROP TABLE IF EXISTS user_profile_tb;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `user_profile_tb` (
                                   `profile_id` binary(16) NOT NULL,
                                   `createdAt` datetime(6) DEFAULT NULL,
                                   `modifiedAt` datetime(6) DEFAULT NULL,
                                   `city` varchar(255) DEFAULT NULL,
                                   `detailed` varchar(255) DEFAULT NULL,
                                   `district` varchar(255) DEFAULT NULL,
                                   `id` binary(16) DEFAULT NULL,
                                   `postCode` varchar(255) DEFAULT NULL,
                                   `street` varchar(255) DEFAULT NULL,
                                   `age` int DEFAULT NULL,
                                   `email` varchar(255) DEFAULT NULL,
                                   `name` varchar(255) DEFAULT NULL,
                                   `phone` varchar(255) DEFAULT NULL,
                                   `residentNumber` varchar(255) DEFAULT NULL,
                                   `role` enum('ADMIN','CORPORATE','GROUP','INDIVIDUAL','NONE') DEFAULT NULL,
                                   PRIMARY KEY (`profile_id`),
                                   UNIQUE KEY `UKdf9vhitrg74tkc98qps128u3y` (`residentNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_credential_tb` (
                                      `credential_id` binary(16) NOT NULL,
                                      `createdAt` datetime(6) DEFAULT NULL,
                                      `modifiedAt` datetime(6) DEFAULT NULL,
                                      `password` varchar(255) DEFAULT NULL,
                                      `userid` varchar(255) NOT NULL,
                                      `username` varchar(255) NOT NULL,
                                      `profile_id` binary(16) DEFAULT NULL,
                                      PRIMARY KEY (`credential_id`),
                                      UNIQUE KEY `UKjaxje5ncv3y3tupck2nu6xfb8` (`userid`,`password`),
                                      KEY `FKd8qry1avsq2fymomscukvt1ak` (`profile_id`),
                                      CONSTRAINT `FKd8qry1avsq2fymomscukvt1ak` FOREIGN KEY (`profile_id`) REFERENCES `user_profile_tb` (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO user_profile_tb (profile_id, createdAt, modifiedAt, city, detailed, district, id, postCode, street, age, email, name, phone, residentNumber, role)
VALUES
    (UNHEX('C1F6A6B8D6F6A5C7'), NOW(), NOW(), 'Seoul', 'GangDong', 'gangdong-gu', UNHEX('4A6F686E446F65'), '06234', 'Teheran-ro', 30, 'john.doe@example.com', 'John Doe', '010-1234-5678', '123456-1234567', 'INDIVIDUAL'),
    (UNHEX('A2B6C7D8E8F9C6D1'), NOW(), NOW(), 'Busan', 'Haeundae', 'Haeundae-gu', UNHEX('53616E646572736F6E'), '48060', 'Haeundae-ro', 28, 'susan.lee@example.com', 'Susan Lee', '010-9876-5432', '234567-2345678', 'CORPORATE');

INSERT INTO user_credential_tb (credential_id, createdAt, modifiedAt, password, userid, username, profile_id)
VALUES
    (UNHEX('4A6F686E446F65'), NOW(), NOW(), '$2a$10$QmS8GgJvzzvhxM1pv8N12OjKPmS0JH26a8k95V6Jw.mjj8f/R9kFi:', 'john_doe', 'John Doe', UNHEX('C1F6A6B8D6F6A5C7')),
    (UNHEX('53616E646572736F6E'), NOW(), NOW(), '$2a$10$QmS8GgJvzzvhxM1pv8N12OjKPmS0JH26a8k95V6Jw.mjj8f/R9kFi:', 'susan_lee', 'Susan Lee', UNHEX('A2B6C7D8E8F9C6D1'));