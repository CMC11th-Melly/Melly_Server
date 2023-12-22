FROM openjdk:17-oracle

# EC2 내에 Code Deploy를 통해 배포한 뒤 jar 파일을 app.jar라는 이름으로 복사한다
COPY ./*.jar app.jar

# Jar 파일을 실행한다
ENTRYPOINT ["java",
            "-Xms1g", "-Xmx1g", "-XX:NewRatio=3", "-verbosegc", "-server", "-XX:+HeapDumpOnOutOfMemoryError",
            "-jar", "-Dspring.profiles.active=prod", "app.jar"]


