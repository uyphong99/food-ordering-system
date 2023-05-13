#!/bin/bash

docker compose -f common.yml -f zookeeper.yml up -d
docker compose -f common.yml -f kafka_cluster.yml up -d
docker compose -f common.yml -f init_kafka.yml up -d
docker compose -f common.yml -f postgres.yml up -d
