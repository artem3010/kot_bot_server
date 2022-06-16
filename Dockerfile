FROM openjdk:11
ADD target/demo-0.0.1-SNAPSHOT.jar  app.jar
ADD ./cat ./cat
ENTRYPOINT ["java","-jar","/app.jar"]
