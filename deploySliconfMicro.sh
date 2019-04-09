#!/bin/sh
git pull
docker-compose down && docker-compose up -d --build
docker network connect sliconf sliconf-micro-container
docker restart nginx1