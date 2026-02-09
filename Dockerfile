# ---------- build ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

COPY . .
RUN mvn -q -DskipTests clean package

# ---------- runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# healthcheck no compose usa curl
RUN apt-get update \
  && apt-get install -y --no-install-recommends curl \
  && rm -rf /var/lib/apt/lists/*

RUN addgroup --system --gid 10001 app \
  && adduser --system --uid 10001 --ingroup app --home /app app

COPY --from=build --chown=app:app /app/target/*.jar /app/app.jar

EXPOSE 8080

USER app
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC"

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
