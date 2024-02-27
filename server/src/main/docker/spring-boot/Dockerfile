FROM eclipse-temurin:17-jdk-alpine
LABEL org.opencontainers.image.authors="John Reynolds <john.reynolds.irl@acm.org>"

ADD /maven/server*.war app.war

ENTRYPOINT ["java","-jar","/app.war"]