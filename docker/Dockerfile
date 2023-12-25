FROM openjdk:17-oracle

# EC2 내에 Code Deploy를 통해 배포한 뒤 jar 파일을 app.jar라는 이름으로 복사한다
COPY ./*.jar app.jar

# Jar 파일을 실행한다
ENTRYPOINT ["java", "-jar", "-javaagent:./pinpoint-agent-2.5.3/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.applicationName=melly", "-Dpinpoint.config=./pinpoint-agent-2.5.3/pinpoint-root.config", "-Dspring.profiles.active=prod", "app.jar"]


