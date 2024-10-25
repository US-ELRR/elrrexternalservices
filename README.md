# elrrexternalservices
Services for integrating with external interfaces (LRS, CaSS, ECC etc)

Setup elrrdatasync first [README](../elrrdatasync/README.md)

# Dependencies:
- Java JDK 17
- git
- Maven 3
- Docker Desktop

# Tools
- DBeaver
- Postman
- Eclipse or other IDE

# Build the application
- mvn clean install

# Run Postman to populate lrs-db
1. Postman
   a. POST http://localhost:8083/xapi/statements
   b. Headers
      1. key = X-Experience-API-Version
      2. value = 1.0.3
   c. Body raw, JSON
[
  {"actor":{"name":"John Doe","mbox":"mailto:JohnDoe@gmailcom"},"verb":{"id":"https://adlnet.gov/expapi/verbs/achieved","display":{"en-us":"Achieved"}},"object":{"id":"https://w3id.org/xapi/credential/GIAC%20Security%20Essentials%20Certification%20%28GSEC%29","objectType":"Activity","definition":{"name":{"en-us":"GIAC Security Essentials Certification (GSEC)"},"type":"https://yetanalytics.com/deloitte-edlm/demo-profile/certificate"}},"stored":"2024-09-20T21:37:23.835000000Z","authority":{"account":{"homePage":"http://example.org","name":"0192115b-03d0-849f-8a65-f217ffbe2207"},"objectType":"Agent"},"id":"d9f1328b-bcc2-4b9c-b954-03cb88a240c8","timestamp":"2024-09-20T21:37:23.835000000Z","version":"1.0.0"}
]

# Deploying the application to Docker 
The easiest way to deploy the sample application to Docker is to follow below steps:

1. mvn clean install -Dmaven.test.skip=false

2. mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

3. docker build --build-arg JAR_FILE="./target/elrrexternalservices-0.0.1-SNAPSHOT.jar" --file Dockerfile -t <docker_hub>/newrepository:elrrexternalservices-dck-img .

4. docker run -p 8088:8088 -t <docker_hub>/newrepository:elrrexternalservices-dck-img

# Running the application using the Spring Boot Maven plugin: 
- mvn clean
- mvn spring-boot:run -D"spring-boot.run.profiles"=local -e (Windows)
- mvn spring-boot:run -D spring-boot.run.profiles=local -e  (Linux)
- Ctrl+C to end --> Terminate batch job = Y

# Optional step 
1. docker push <docker_hub>/newrepository:elrrexternalservices-dck-img