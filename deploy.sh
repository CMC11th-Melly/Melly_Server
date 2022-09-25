#!/bin/bash

REPOSITORY=/home/ubuntu/melly
cd $REPOSITORY

APP_NAME=melly
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'mellyServer-0.0.1-SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo  "> 종료할 것 없음"
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar -Dserver.tomcat.accesslog.enabled=true -Dserver.tomcat.basedir=/home/ubuntu -Dspring.config.location=/home/ubuntu/melly/src/main/resources/application.yml $JAR_PATH > /home/ubuntu/nohup.out 2>&1 --logging.file.path=/home/ubuntu/ &

echo "> finished"
exit 0