FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /workspace

COPY pom.xml .
COPY dominio-compartilhado/pom.xml dominio-compartilhado/
COPY dominio-adm/pom.xml dominio-adm/
COPY dominio-comunidade/pom.xml dominio-comunidade/
COPY dominio-loja/pom.xml dominio-loja/
COPY aplicacao/pom.xml aplicacao/
COPY infraestrutura/pom.xml infraestrutura/
COPY ui-backend/pom.xml ui-backend/

RUN --mount=type=cache,target=/root/.m2 mvn -B -pl ui-backend -am dependency:go-offline || true

COPY dominio-compartilhado dominio-compartilhado
COPY dominio-adm dominio-adm
COPY dominio-comunidade dominio-comunidade
COPY dominio-loja dominio-loja
COPY aplicacao aplicacao
COPY infraestrutura infraestrutura
COPY ui-backend ui-backend

RUN --mount=type=cache,target=/root/.m2 mvn -B -pl ui-backend -am -DskipTests package \
    && cp ui-backend/target/tcghub-ui-backend-*.jar /workspace/app.jar

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /workspace/app.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
