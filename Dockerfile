FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /src
COPY . .
RUN mvn -q -B -DskipTests clean package

FROM eclipse-temurin:21-jre AS auth-runtime
WORKDIR /app
COPY --from=build /src/auth-service/target/auth-service-*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM eclipse-temurin:21-jre AS account-runtime
WORKDIR /app
COPY --from=build /src/account-service/target/account-service-*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM eclipse-temurin:21-jre AS item-runtime
WORKDIR /app
COPY --from=build /src/item-service/target/item-service-*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM eclipse-temurin:21-jre AS order-runtime
WORKDIR /app
COPY --from=build /src/order-service/target/order-service-*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","/app/app.jar"]

FROM eclipse-temurin:21-jre AS payment-runtime
WORKDIR /app
COPY --from=build /src/payment-service/target/payment-service-*.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/app/app.jar"]
