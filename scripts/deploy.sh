#!/bin/bash

# 작업 디렉토리를 /home/ec2-user/app으로 변경
cd /home/ec2-user/app

DOCKER_APP_NAME=melly

EXIST_SERVICE=$(sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml ps | grep Up)

if [ -z "$EXIST_SERVICE" ]; then
	sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml up -d --build
else
  sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml down
  sudo docker image prune -af
  sleep 10
  sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml up -d --build
fi
