FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace/app

COPY pom.xml .
COPY src src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
# Setup tesseract data directory if needed by tess4j
RUN mkdir -p /tessdata
ENV APP_OCR_TESSDATA_PATH=/tessdata
ENTRYPOINT ["java","-jar","/app.jar"]
