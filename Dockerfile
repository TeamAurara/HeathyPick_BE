FROM amazoncorretto:21-alpine

COPY build/libs/eolala-0.0.1-SNAPSHOT.jar healthpick.jar

ENV TZ=Asia/Seoul
ARG ENV

ENTRYPOINT ["java", "-Xms512m", "-Dspring.profiles.active=prod", "-jar", "healthpick.jar"]
