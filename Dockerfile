# ---- Build Stage ----
FROM gradle:8.9.0-jdk-17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean bootJar -x test

# ---- Run Stage ----
FROM eclipse-temurin:17-jre-alpine
ENV TZ=Asia/Seoul
WORKDIR /app
COPY --from=builder /app/build/libs/*SNAPSHOT*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]