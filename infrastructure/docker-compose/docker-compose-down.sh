#!/bin/bash

docker compose -f common.yml -f zookeeper.yml down
docker compose -f common.yml -f kafka_cluster.yml down
docker compose -f common.yml -f init_kafka.yml down
docker compose -f common.yml -f postgres.yml down

