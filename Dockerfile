FROM openjdk:21
LABEL authors="alex"
EXPOSE 8080
ADD backend/target/app.jar app.jar
CMD ["sh", "-c", "java -jar /app.jar"]