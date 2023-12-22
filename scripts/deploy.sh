#!/bin/bash

# app 디렉토리로 이동
cd /home/ec2-user/app

DOCKER_APP_NAME=melly

# 현재 melly 이라는 이름으로 동작중인 도커 컨테이너가 있는지 체크
EXIST_SERVICE=$(sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml ps | grep Up)

# 만약 현재 동작중인 프로세스가 없다면 docker-compose.yml 파일 기반으로 실행
if [ -z "$EXIST_SERVICE" ]; then
	sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml up -d --build
else
# 기존에 동작중인 프로세스가 있다면 서비스 종료 후 새로운 버전 실행
  sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml down
  sudo docker image prune -af
  sleep 10
  sudo docker-compose -p ${DOCKER_APP_NAME} -f docker-compose.yml up -d --build
fi
