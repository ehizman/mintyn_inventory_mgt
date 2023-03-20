#!/bin/sh
docker rm -f mongodatabase
docker run -d --network mongonetwork --name mongodatabase -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
  -e MONGO_INITDB_ROOT_PASSWORD=Testing1234# \
  mongo

docker ps