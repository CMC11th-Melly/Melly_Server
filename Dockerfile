FROM openjdk:17-oracle

COPY ./core-api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]

#### Docker 이미지를 생성할 때 기반이 되는 베이스 이미지를 설정한다.
#FROM openjdk:11-jre-slim
#### Dockerfile 내에서 사용할 변수 JAR_FILE을 정의한다.
#ARG JAR_FILE=build/libs/*.jar
#### JAR_FILE 경로에 해당하는 파일을 Docker 이미지 내부로 복사한다.
#COPY ${JAR_FILE} {원하는 파일 이름.jar}
#### Docker 컨테이너가 시작될 때 실행할 명령을 지정한다.
#ENTRYPOINT ["java","-jar","/{원하는 파일 이름.jar}"]
