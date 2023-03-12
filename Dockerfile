FROM openjdk:11

ARG id
ARG secret

ENV ID=${id}
ENV SECRET=${secret}

COPY app/target/customerms.jar /target/customerms.jar

EXPOSE 8080

CMD ["sh","-c","java -jar -Dspring.profiles.active=deploy -Dproductms.clientId=${ID} -Dproductms.clientSecret=${SECRET} /target/customerms.jar"]