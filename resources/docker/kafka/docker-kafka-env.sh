#!/bin/bash

kafka-topics --create --topic family.invitation --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic family.payment --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
kafka-topics --create --topic family.cash.conversion --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
