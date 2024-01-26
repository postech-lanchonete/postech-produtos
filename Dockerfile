FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY . .

RUN ./gradlew assemble

FROM openjdk:17-oracle

WORKDIR /app

COPY --from=builder /app/build/libs/postech-produtos-1.0.0-POC.jar .

EXPOSE 8080

CMD ["java", "-jar", "postech-produtos-1.0.0-POC.jar"]