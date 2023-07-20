FROM adoptopenjdk/openjdk11

COPY ./melly-api/build/libs/melly-api-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]