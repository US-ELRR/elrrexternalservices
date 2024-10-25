# elrrexternalservices API
Services for integrating with external interfaces (LRS, CaSS, ECC etc)

Setup elrrdatasync first.

# Dependencies:
- Java JDK 17
- git
- Maven 3
- Docker Desktop

# Tools
- DBeaver
- Postman

# Run elrrexternalservices
- Update application-local.properties to match docker-compose.yml
- Start Docker Desktop
- Open terminal
- git switch <dev feature branch>
- mvn clean
- mvn spring-boot:run -D"spring-boot.run.profiles"=local -e (Windows)
- mvn spring-boot:run -D spring-boot.run.profiles=local -e (Linux)
- Ctrl+C to end --> Terminate batch job = Y# elrrexternalservices API