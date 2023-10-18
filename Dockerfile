FROM openjdk:17-jdk-slim
ADD build/libs/music-bot-0.0.1-SNAPSHOT.jar /usr/local/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "/usr/local/app.jar"]