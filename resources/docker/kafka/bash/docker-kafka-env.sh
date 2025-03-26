#!/bin/bash

kafka-topics --create --topic user.account.created --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

kafka-topics --create --topic family.invitation --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic family.payment.request --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic family.payment.approval --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic family.cash.conversion --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1

kafka-topics --create --topic pay.cachable.charged --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic pay.product.purchased --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1