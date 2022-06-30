# elrrexternalservices API
Services for integrating with external interfaces (LRS, CaSS, ECC etc)

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

# Requirements
For building and running the elrrexternalservices you need:

`Java >=1.8` : Download and install Java from here 
* [Java](https://www.oracle.com/java/technologies/downloads/).

`Maven >=3.6` : Download and install Apache Maven from here 
* [Maven](https://maven.apache.org/) - Dependency Management.

`Postgres >=11` : Download and install Postgres from here 
* [Postgres](https://www.postgresql.org/download/). 

`Docker` : Download and install Docker from here 
*[Docker](https://www.docker.com/products/docker-desktop).

# Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the com.deloitte.elrr.ElrrExternalServicesApplication class from your IDE

1. Clone the Github repository:

   [GitHub-US-ELRR](https://github.com/US-ELRR/elrrservices)

2. Open terminal at the root directory of the project.
    
    example: ~/elrrexternalservices

3. Run command to install all the requirements from requirements.txt 
    
    mvn spring-boot:run

4. Once the installation and build are done, Once the server is up you can   access API using below URL
    
    http://localhost:8088/api/lrsdata?lastReadDate=<Date a parameter in this format>
	
	e.g 2021-01-10T00:00:00Z

# Deploying the application to Docker 
The easiest way to deploy the sample application to Docker is to follow below steps:

1. mvn clean install -Dmaven.test.skip=false

2. mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

3. docker build --build-arg JAR_FILE="./target/elrrexternalservices-0.0.1-SNAPSHOT.jar" --file Dockerfile -t <docker_hub>/newrepository:elrrexternalservices-dck-img .

4. docker run -p 8088:8088 -t <docker_hub>/newrepository:elrrexternalservices-dck-img

# Optional step 
1. docker push <docker_hub>/newrepository:elrrexternalservices-dck-img


