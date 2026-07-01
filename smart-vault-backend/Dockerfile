FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
# Setup tesseract data directory if needed by tess4j
RUN mkdir -p /tessdata
ENV APP_OCR_TESSDATA_PATH=/tessdata
ENTRYPOINT ["java","-jar","/app.jar"]
