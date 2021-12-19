FROM maven:3-openjdk-17-slim AS build
COPY . /pizzaserver
WORKDIR /pizzaserver
RUN mvn clean package

FROM openjdk:17-slim
COPY --from=build /pizzaserver/pizzaserver-server/target/PizzaServer-Server-1.0-SNAPSHOT.jar /PizzaServer.jar
ENTRYPOINT ["java", "-jar", "./PizzaServer.jar"]