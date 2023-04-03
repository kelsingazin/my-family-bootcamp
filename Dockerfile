FROM eclipse-temurin:17-jre-jammy
ENV TZ="Asia/Almaty" JAVA_FLAGS="-Xmx500m" LIQUIBASE_ENABLED=true
WORKDIR /app
EXPOSE 8080
COPY target/*.jar team4-service.jar
ENTRYPOINT ["sh", "-c", "java -jar team4-service.jar $JAVA_FLAGS"]
