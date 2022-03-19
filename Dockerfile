FROM gradle:7.4-jdk17 AS BUILD

WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar

FROM bellsoft/liberica-openjdk-alpine:17

WORKDIR /app
COPY --from=BUILD /app/build/libs/work-scheduler*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
