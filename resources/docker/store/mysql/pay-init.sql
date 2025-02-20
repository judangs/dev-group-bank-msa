DROP DATABASE IF EXISTS pay;
CREATE DATABASE pay;
USE pay;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS pay_cash_tb;
DROP TABLE IF EXISTS pay_cash_reserved_charge_tb;
DROP TABLE IF EXISTS pay_family_tb;
DROP TABLE IF EXISTS pay_family_participant_tb;
DROP TABLE IF EXISTS pay_owner_tb;
DROP TABLE IF EXISTS pay_payment_card_tb;
DROP TABLE IF EXISTS pay_history_tb;

SET FOREIGN_KEY_CHECKS = 1;


CREATE TABLE `pay_owner_tb` (
                                `ownerId` binary(16) NOT NULL,
                                `createdAt` datetime(6) DEFAULT NULL,
                                `modifiedAt` datetime(6) DEFAULT NULL,
                                `roles` varchar(255) DEFAULT NULL,
                                `email` varchar(255) DEFAULT NULL,
                                `userid` varchar(255) DEFAULT NULL,
                                `username` varchar(255) DEFAULT NULL,
                                PRIMARY KEY (`ownerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pay_payment_card_tb` (
                                       `cardId` binary(16) NOT NULL,
                                       `createdAt` datetime(6) DEFAULT NULL,
                                       `modifiedAt` datetime(6) DEFAULT NULL,
                                       `cardName` varchar(255) DEFAULT NULL,
                                       `cardNumber` varchar(255) DEFAULT NULL,
                                       `cvc` varchar(255) DEFAULT NULL,
                                       `expireDate` varchar(255) DEFAULT NULL,
                                       `passwordStartwith` varchar(255) DEFAULT NULL,
                                       `owner_id` binary(16) NOT NULL,
                                       PRIMARY KEY (`cardId`),
                                       KEY `FK5lsei8yut3kmvblqe5ep5yk5e` (`owner_id`),
                                       CONSTRAINT `FK5lsei8yut3kmvblqe5ep5yk5e` FOREIGN KEY (`owner_id`) REFERENCES `pay_owner_tb` (`ownerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `pay_cash_tb` (
                               `cashId` binary(16) NOT NULL,
                               `createdAt` datetime(6) DEFAULT NULL,
                               `modifiedAt` datetime(6) DEFAULT NULL,
                               `credit` decimal(38,2) DEFAULT NULL,
                               `currency` decimal(30,2) DEFAULT NULL,
                               `perDaily` decimal(30,2) DEFAULT NULL,
                               `perOnce` decimal(30,2) DEFAULT NULL,
                               `payOwner_ownerId` binary(16) DEFAULT NULL,
                               PRIMARY KEY (`cashId`),
                               UNIQUE KEY `UKdohonqh6swl5o9od09h026jj8` (`payOwner_ownerId`),
                               CONSTRAINT `FKaelbpf7u3hr2jcva6hnl2w6wq` FOREIGN KEY (`payOwner_ownerId`) REFERENCES `pay_owner_tb` (`ownerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pay_cash_reserved_charge_tb` (
                                               `id` binary(16) NOT NULL,
                                               `createdAt` datetime(6) DEFAULT NULL,
                                               `modifiedAt` datetime(6) DEFAULT NULL,
                                               `amount` decimal(38,2) DEFAULT NULL,
                                               `scheduledDate` date DEFAULT NULL,
                                               `triggerBalance` decimal(38,2) DEFAULT NULL,
                                               `type` enum('BALANCE','DATE') DEFAULT NULL,
                                               `card_cardId` binary(16) DEFAULT NULL,
                                               `cash_cashId` binary(16) DEFAULT NULL,
                                               PRIMARY KEY (`id`),
                                               UNIQUE KEY `UKxx1o3pmwlgggg0dg6n3rsavf` (`card_cardId`),
                                               UNIQUE KEY `UKauci6lyiqmkhemerv299a13gd` (`cash_cashId`),
                                               CONSTRAINT `FK5jttyvfe1qoaq2k0xxs49scg6` FOREIGN KEY (`cash_cashId`) REFERENCES `pay_cash_tb` (`cashId`),
                                               CONSTRAINT `FKrmid0eqfnux7ykmeygkeduq6t` FOREIGN KEY (`card_cardId`) REFERENCES `pay_payment_card_tb` (`cardId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pay_family_tb` (
                                 `family_id` binary(16) NOT NULL,
                                 `createdAt` datetime(6) DEFAULT NULL,
                                 `modifiedAt` datetime(6) DEFAULT NULL,
                                 `credit` decimal(30,10) DEFAULT NULL,
                                 `enrollmentDate` datetime(6) DEFAULT NULL,
                                 `memberId` binary(16) DEFAULT NULL,
                                 `terminateDate` datetime(6) DEFAULT NULL,
                                 `withdrawal` bit(1) DEFAULT NULL,
                                 `email` varchar(255) DEFAULT NULL,
                                 `userid` varchar(255) DEFAULT NULL,
                                 `username` varchar(255) DEFAULT NULL,
                                 PRIMARY KEY (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pay_family_participant_tb` (
                                             `family_id` binary(16) NOT NULL,
                                             `enrollmentDate` datetime(6) DEFAULT NULL,
                                             `memberId` binary(16) DEFAULT NULL,
                                             `terminateDate` datetime(6) DEFAULT NULL,
                                             `withdrawal` bit(1) DEFAULT NULL,
                                             `email` varchar(255) DEFAULT NULL,
                                             `userid` varchar(255) DEFAULT NULL,
                                             `username` varchar(255) DEFAULT NULL,
                                             KEY `FK5mgxm0xgq22k96psm1449gy5l` (`family_id`),
                                             CONSTRAINT `FK5mgxm0xgq22k96psm1449gy5l` FOREIGN KEY (`family_id`) REFERENCES `pay_family_tb` (`family_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pay_history_tb` (
                                  `history_type` varchar(31) NOT NULL,
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `createdAt` datetime(6) DEFAULT NULL,
                                  `modifiedAt` datetime(6) DEFAULT NULL,
                                  `method` enum('CARD','CASH','TRANSFER') DEFAULT NULL,
                                  `balance` decimal(38,2) DEFAULT NULL,
                                  `payName` varchar(255) DEFAULT NULL,
                                  `rollbackDate` datetime(6) DEFAULT NULL,
                                  `transactionDate` datetime(6) DEFAULT NULL,
                                  `userId` varchar(255) DEFAULT NULL,
                                  `transferTo` varchar(255) DEFAULT NULL,
                                  `cardNumber` varchar(255) DEFAULT NULL,
                                  `orderId` varchar(255) DEFAULT NULL,
                                  `storeName` varchar(255) DEFAULT NULL,
                                  `username` varchar(255) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `pay_owner_tb` (`ownerId`, `createdAt`, `modifiedAt`, `roles`, `email`, `userid`, `username`)
VALUES
    (UNHEX('C1F6A6B8D6F6A5C7'), NOW(), NOW(), 'OWNER', 'owner@example.com', 'owner_user', 'Owner User');
INSERT INTO `pay_payment_card_tb` (`cardId`, `createdAt`, `modifiedAt`, `cardName`, `cardNumber`, `cvc`, `expireDate`, `passwordStartwith`, `owner_id`)
VALUES
    (UNHEX('A2B6C7D8E8F9C6D1'), NOW(), NOW(), 'Visa', '4111111111111111', '123', '12/25', '12', UNHEX('C1F6A6B8D6F6A5C7'));
INSERT INTO `pay_cash_tb` (`cashId`, `createdAt`, `modifiedAt`, `credit`, `currency`, `perDaily`, `perOnce`, `payOwner_ownerId`)
VALUES
    (UNHEX('4A6F686E446F65'), NOW(), NOW(), 1000.00, 1.00, 100.00, 50.00, UNHEX('C1F6A6B8D6F6A5C7'));

INSERT INTO `pay_cash_reserved_charge_tb` (`id`, `createdAt`, `modifiedAt`, `amount`, `scheduledDate`, `triggerBalance`, `type`, `card_cardId`, `cash_cashId`)
VALUES
    (UNHEX('53616E646572736F6E'), NOW(), NOW(), 500.00, '2023-12-31', 100.00, 'BALANCE', UNHEX('A2B6C7D8E8F9C6D1'), UNHEX('4A6F686E446F65'));

INSERT INTO `pay_family_tb` (`family_id`, `createdAt`, `modifiedAt`, `credit`, `enrollmentDate`, `memberId`, `terminateDate`, `withdrawal`, `email`, `userid`, `username`)
VALUES
    (UNHEX('A1B2C3D4E5F6A7B8'), NOW(), NOW(), 2000.00, NOW(), UNHEX('B1C2D3E4F5A6B7C8'), NULL, 0, 'family@example.com', 'family_user', 'Family User');

INSERT INTO `pay_family_participant_tb` (`family_id`, `enrollmentDate`, `memberId`, `terminateDate`, `withdrawal`, `email`, `userid`, `username`)
VALUES
    (UNHEX('A1B2C3D4E5F6A7B8'), NOW(), UNHEX('B1C2D3E4F5A6B7C8'), NULL, 0, 'participant@example.com', 'participant_user', 'Participant User');

INSERT INTO `pay_history_tb` (`history_type`, `id`, `createdAt`, `modifiedAt`, `method`, `balance`, `payName`, `rollbackDate`, `transactionDate`, `userId`, `transferTo`, `cardNumber`, `orderId`, `storeName`, `username`)
VALUES
    ('PAYMENT', 1, NOW(), NOW(), 'CARD', 100.00, 'Payment Name', NULL, NOW(), 'user_id', 'transfer_to', '4111111111111111', 'order_id', 'Store Name', 'User Name');
