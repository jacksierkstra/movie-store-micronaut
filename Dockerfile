FROM openjdk:8-jdk-alpine as BUILD

COPY . /usr/src/app
WORKDIR /usr/src/app
#RUN ./gradlew build

FROM openjdk:8-jre-alpine
EXPOSE 8080
COPY --from=BUILD /usr/src/app/build/libs /opt/target
WORKDIR /opt/target

CMD ["java", "-jar", "movie-store-micronaut-0.1-all.jar"]