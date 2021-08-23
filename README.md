# elrrexternalservices
Services for integrating with external interfaces (LRS, CaSS, ECC etc)

# Requirements
For building and running the elrrexternalservices you need:
JDK 1.8
Maven 3
# Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the com.deloitte.elrr.ElrrExternalServicesApplication class from your IDE

# Alternatively you can use the Spring Boot Maven plugin like so: 
mvn spring-boot:run

# Deploying the application to Docker 
The easiest way to deploy the sample application to Docker is to follow below steps:

mvn clean install -Dmaven.test.skip=false
mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)
docker build --build-arg JAR_FILE="./target/elrrexternalservices-0.0.1-SNAPSHOT.jar" --file Dockerfile -t <docker_hub>/newrepository:elrrexternalservices-dck-img .
docker run -p 8088:8088 -t <docker_hub>/newrepository:elrrexternalservices-dck-img



