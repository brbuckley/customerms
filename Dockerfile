FROM openjdk:11

COPY app/target/customerms.jar /target/customerms.jar

EXPOSE 8080

CMD ["java","-jar","-Dspring.profiles.active=deploy","/target/customerms.jar"]