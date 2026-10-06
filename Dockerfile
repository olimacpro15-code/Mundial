FROM openjdk:21
COPY "./target/mundial-api-rest-web-1.0.0.jar" "app.jar"
EXPOSE 8101
ENTRYPOINT [ "java", "-jar", "app.jar" ]