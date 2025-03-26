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

create table family_card_tb (
        cardId BINARY(16) not null,
        primary key (cardId)
) engine=InnoDB;

create table owner_pay_card_tb (
       cardId BINARY(16) not null,
       owner_id BINARY(16) not null,
       primary key (cardId)
) engine=InnoDB;

create table pay_card_tb (
     cardId BINARY(16) not null,
     createdAt datetime(6),
     modifiedAt datetime(6),
     cardName varchar(255),
     cardNumber varchar(255),
     cvc varchar(255),
     expireDate varchar(255),
     passwordStartwith varchar(255),
     type enum ('CORPORATE','FAMILY','PERSONAL'),
     cash_cashId BINARY(16),
     primary key (cardId)
) engine=InnoDB;

create table pay_cash_reserved_charge_tb (
     id BINARY(16) not null,
     createdAt datetime(6),
     modifiedAt datetime(6),
     credit decimal(30,2),
     cardId binary(16),
     scheduledDate date,
     balance decimal(38,2),
     type enum ('BALANCE','DATE'),
     primary key (id)
) engine=InnoDB;

create table pay_cash_tb (
     cashId BINARY(16) not null,
     createdAt datetime(6),
     modifiedAt datetime(6),
     credit decimal(30,2),
     currency decimal(30,2),
     perDaily decimal(30,2),
     perOnce decimal(30,2),
     ownerId varchar(255),
     primary key (cashId)
) engine=InnoDB;

create table pay_family_invitation_tb (
      id BINARY(16) not null,
      createdAt datetime(6),
      modifiedAt datetime(6),
      familyId binary(16),
      status enum ('ACCEPTED','EXPIRED','PENDING','REJECTED'),
      expiryDate datetime(6),
      enrollmentDate datetime(6),
      memberId binary(16),
      terminateDate datetime(6),
      withdrawal bit,
      email varchar(255),
      userid varchar(255),
      username varchar(255),
      primary key (id)
) engine=InnoDB;

create table pay_family_participant_tb (
       family_id BINARY(16) not null,
       enrollmentDate datetime(6),
       memberId binary(16),
       terminateDate datetime(6),
       withdrawal bit,
       email varchar(255),
       userid varchar(255),
       username varchar(255)
) engine=InnoDB;

create table pay_family_payment_products_tb (
        `request-payment-id` BINARY(16) not null,
        cardId binary(16),
        name TEXT,
        price decimal(30,2),
        quantity integer
) engine=InnoDB;

create table pay_family_payment_tb (
       id BINARY(16) not null,
       createdAt datetime(6),
       modifiedAt datetime(6),
       familyId binary(16),
       status enum ('ACCEPTED','EXPIRED','PENDING','REJECTED'),
       enrollmentDate datetime(6),
       memberId binary(16),
       terminateDate datetime(6),
       withdrawal bit,
       email varchar(255),
       userid varchar(255),
       username varchar(255),
       requestDate datetime(6),
       primary key (id)
) engine=InnoDB;

create table pay_family_tb (
       family_id BINARY(16) not null,
       createdAt datetime(6),
       modifiedAt datetime(6),
       enrollmentDate datetime(6),
       memberId binary(16),
       terminateDate datetime(6),
       withdrawal bit,
       email varchar(255),
       userid varchar(255),
       username varchar(255),
       familyCard_cardId BINARY(16),
       primary key (family_id)
) engine=InnoDB;

create table pay_history_tb (
        history_type varchar(31) not null,
        id bigint not null auto_increment,
        createdAt datetime(6),
        modifiedAt datetime(6),
        enrollmentDate datetime(6),
        memberId binary(16),
        terminateDate datetime(6),
        withdrawal bit,
        email varchar(255),
        userid varchar(255),
        username varchar(255),
        method enum ('CARD','CASH','TRANSFER'),
        balance decimal(38,2),
        payName varchar(255),
        paymentId varchar(255),
        rollbackDate datetime(6),
        transactionDate datetime(6),
        storeName varchar(255),
        transferTo varchar(255),
        primary key (id)
) engine=InnoDB;

create table pay_owner_tb (
      owner_id BINARY(16) not null,
      createdAt datetime(6),
      modifiedAt datetime(6),
      roles varchar(255),
      email varchar(255),
      userid varchar(255),
      username varchar(255),
      primary key (owner_id)
) engine=InnoDB;


alter table pay_card_tb add constraint UK6wtogclnp79fucetpjvs0kj1p unique (cash_cashId);
alter table pay_family_participant_tb add constraint UK2i2w9sgvh2conyrqw2x03566e unique (family_id, userid);
alter table pay_family_payment_products_tb add constraint UK5embptf3grvnpbn79qr71aiem unique (`request-payment-id`, name(255));
alter table pay_family_tb add constraint UKmgo9lxtlt2f7ryhk9vw9hjp5f unique (familyCard_cardId);
alter table family_card_tb add constraint FKj9lji9jlu0vf9a45ju27wvvyx foreign key (cardId) references pay_card_tb (cardId);
alter table owner_pay_card_tb add constraint FKhehemdd9goyp533dduq9h9kpf foreign key (owner_id) references pay_owner_tb (owner_id);
alter table owner_pay_card_tb add constraint FK78v6lmc25x00iylom6jmihvwl foreign key (cardId) references pay_card_tb (cardId);
alter table pay_card_tb add constraint FKfu4ndtbcq22ygb0n50nbhdbsw foreign key (cash_cashId) references pay_cash_tb (cashId);
alter table pay_family_participant_tb add constraint FK5mgxm0xgq22k96psm1449gy5l foreign key (family_id) references pay_family_tb (family_id);
alter table pay_family_payment_products_tb add constraint FKfrq2e7vyowrahbccsripw8r7e foreign key (`request-payment-id`) references pay_family_payment_tb (id);
alter table pay_family_tb add constraint FK7cj8xuv1c36bkkmvxe3u5sqh0 foreign key (familyCard_cardId) references family_card_tb (cardId);

# 1번 유저 정보(swagger default)
INSERT INTO pay_owner_tb
(email, roles, userid, username, createdAt, modifiedAt, owner_id)
VALUES
    ('eotjd228@naver.com', 'ROLE_USER', 'testuser', '테스트', NOW(), NOW(), UUID_TO_BIN('1b6c4a5b-8c7a-47ad-b0c4-5e3b8324a3c1'));
INSERT INTO pay_cash_tb
(createdAt, credit, currency, perDaily, perOnce, modifiedAt, ownerId, cashId)
VALUES
    (NOW(6), 10000, 0, 100000000, 100000000, NOW(6), 'testuser', UNHEX(REPLACE('b4d5a6e7-9c8f-1a2b-c3d4-e5f6a7b8c9d0', '-', '')));
INSERT INTO pay_card_tb
(cardName, cardNumber, cash_cashId, createdAt, cvc, expireDate, modifiedAt, passwordStartwith, type, cardId)
VALUES
    ('Visa', '1234 5678 9876 5432', UNHEX(REPLACE('b4d5a6e7-9c8f-1a2b-c3d4-e5f6a7b8c9d0', '-', '')), NOW(6), '123', '12/25', NOW(6), 'V', 'CORPORATE', UNHEX(REPLACE('e1f2d3c4-b5a6-7b8c-9d10-11e12f13a14b', '-', '')));
INSERT INTO owner_pay_card_tb
(owner_id, cardId)
VALUES
    (UUID_TO_BIN('1b6c4a5b-8c7a-47ad-b0c4-5e3b8324a3c1'), UNHEX(REPLACE('e1f2d3c4-b5a6-7b8c-9d10-11e12f13a14b', '-', '')));


#2번 유저 정보
INSERT INTO pay_owner_tb
(email, roles, userid, username, createdAt, modifiedAt, owner_id)
VALUES
    ('follower@naver.com', 'ROLE_USER', 'follower', '팔로워', NOW(), NOW(), UUID_TO_BIN('a61e349d-d6b1-45d9-8d65-2995304d87c6'));
INSERT INTO pay_cash_tb
(createdAt, credit, currency, perDaily, perOnce, modifiedAt, ownerId, cashId)
VALUES
    (NOW(6), 10000, 0, 100000000, 100000000, NOW(6), 'follower', UNHEX(REPLACE('c0dabe7f-f2e4-4c57-b7b0-c1cde72f94b8', '-', '')));
INSERT INTO pay_card_tb
(cardName, cardNumber, cash_cashId, createdAt, cvc, expireDate, modifiedAt, passwordStartwith, type, cardId)
VALUES
    ('Visa', '1234 5678 9876 5432', UNHEX(REPLACE('c0dabe7f-f2e4-4c57-b7b0-c1cde72f94b8', '-', '')), NOW(6), '123', '12/25', NOW(6), 'V', 'CORPORATE', UNHEX(REPLACE('c98d7271-8357-456e-8be4-04a4ccf222c4', '-', '')));
INSERT INTO owner_pay_card_tb
(owner_id, cardId)
VALUES
    (UUID_TO_BIN('a61e349d-d6b1-45d9-8d65-2995304d87c6'), UNHEX(REPLACE('c98d7271-8357-456e-8be4-04a4ccf222c4', '-', '')));


# family 생성
INSERT INTO pay_cash_tb
(createdAt, credit, currency, perDaily, perOnce, modifiedAt, ownerId, cashId)
VALUES
    (NOW(6), 0, 0, 100000000, 100000000, NOW(6), 1, UNHEX(REPLACE('a0eeb2b0-9c1d-4c9a-95cd-635d455f604b', '-', '')));
INSERT INTO pay_card_tb
(cardName, cardNumber, cash_cashId, createdAt, cvc, expireDate, modifiedAt, passwordStartwith, type, cardId)
VALUES
    ('familyCard', '4321 8765 1111 5432', UNHEX(REPLACE('a0eeb2b0-9c1d-4c9a-95cd-635d455f604b', '-', '')), NOW(6), '123', '12/25', NOW(6), 'V', 'CORPORATE', UNHEX(REPLACE('12e5f6a7-8b9c-4a3e-b2f1-c2d3e4f5a6b7', '-', '')));
INSERT INTO family_card_tb
(cardId)
VALUES
    (UNHEX(REPLACE('12e5f6a7-8b9c-4a3e-b2f1-c2d3e4f5a6b7', '-', '')));
INSERT INTO pay_family_tb
(createdAt, familyCard_cardId, email, enrollmentDate, memberId, terminateDate, userid, username, withdrawal, modifiedAt, family_id)
VALUES
    (NOW(6), UNHEX(REPLACE('12e5f6a7-8b9c-4a3e-b2f1-c2d3e4f5a6b7', '-', '')), 'family_member@example.com', NOW(6), 'member-id', NULL, 'testuser', '테스트', 0, NOW(6), UNHEX(REPLACE('d4e5f6a7-8b9c-4a3e-b2f1-c2d3e4f5a6b7', '-', '')));
INSERT INTO pay_family_participant_tb
(family_id, email, enrollmentDate, memberId, terminateDate, userid, username, withdrawal)
VALUES
    (UNHEX(REPLACE('d4e5f6a7-8b9c-4a3e-b2f1-c2d3e4f5a6b7', '-', '')), 'eotjd228@naver.com', NOW(6), UNHEX(REPLACE('9e9534d2-c897-43d9-8bcf-fb63a9708a3d', '-', '')), NULL, 'testuser', '테스트', 0);