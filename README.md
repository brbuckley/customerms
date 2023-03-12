# 7-Customer

## Installation
It is recommended to install the modules separately, the customer module is independent so these commands should do :
```
mvn -f app clean install
```
To install the integration tests we need to first setup the application, database and mocks (if you can't run docker directly from the terminal, you might need to add `sudo docker ...` before the commands) :
```
docker run --name customer-postgres -e POSTGRES_USER=brendan -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=customerms -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres
docker run --name rabbitmq-broker -d -p 5672:5672 rabbitmq

java -jar -Dproductms.clientId=<yourClientId> -Dproductms.clientSecret=<youtClientSecret> app/target/app.jar > app.log 2>&1 &
java -jar test-cases/wiremock/wiremock-jre8-standalone-2.33.2.jar --port 8081 > wiremock.log 2>&1 &

mvn -f test-cases -Drabbitmq.host=localhost -Doauth.clientId=<yourClientId> -Doauth.clientSecret=<yourClientSecret> clean install
```

## Configuration

Please make sure to replace and use your client ids and secrets at the commands. The existing profiles should provide most of the needed configuration, and you can set those properties as environment variables at your machine, pass it as variables as suggested here, or change it manually for ease of use.

## Usage

The easiest way to use the microservice is to run using docker-compose to provide all the needed services (DB, broker, etc) and also the remaining microservices of the system. This docker-compose file can be found at [ToDo url](https://www.google.com)

If you want to run only this MS as a standalone, you'll still need to setup the DB and broker:

```
docker run --name customer-postgres -e POSTGRES_USER=brendan -e POSTGRES_PASSWORD=123456 -e POSTGRES_DB=customerms -e POSTGRES_HOST_AUTH_METHOD=trust -p 5432:5432 -d postgres
docker run --name rabbitmq-broker -d -p 5672:5672 rabbitmq

java -jar -Dproductms.clientId=<yourClientId> -Dproductms.clientSecret=<youtClientSecret> customerms/target/customerms.jar > customerms.log 2>&1 &

# you can check if the service is working by sending the below curls
curl localhost:8080/healthcheck
curl --header "Authorization: Bearer <validToken>" localhost:8080/CST0000001/orders/ORD0000001
```
