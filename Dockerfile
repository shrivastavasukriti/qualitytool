FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 9090
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/qualitytool-0.0.1-SNAPSHOT.jar /app/qualitytool.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/app/qualitytool.jar"]