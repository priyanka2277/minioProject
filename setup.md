AdminStock Project
This is a Spring Boot-based project that includes product owner and admin functionalities.It supports 
product image storage using MinIo,role-based authentication in which admin use certain endpoint with Spring
Security,validation,Flyway for database migration, and Swagger/OpenAPI for API documentation.

Features
Product CRUD operations
Offer CRUD operation
User CRUD operations
Spring Security(In-Memory based)
MySQL database with Flyway migration
MinIO for image uploads
Swagger UI for API testing
Validation with Jakarta
Clean,layered architecture using DTOS and Model Mapper

Tech Stack 
Java 17 
Spring Boot 3.5.0
Spring Data JPA
Spring Security
Flyway
MinIO(Docker)
Swagger/OPENAPI(springdoc-openapi)
Maven

Prerequisites
Java 17
MYSQL Server
Docker (For MinIo)
Maven
IntelliJ IDEA

Setup Instructions
Clone the Repository
git clone https://github.com/priyanka2277/minioProject.git
cd minioproject

Start MySQL
CREATE DATABASE minio;
spring.datasource.url=jdbc:mysql://localhost:3306/minio
spring.datasource.username=root
spring.datasource.password=eng*187#

Start MinIO(Using Docker)
docker run -p 9000:9000 -p 9001:9001 \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  quay.io/minio/minio server /data --console-address ":9001"
Visit :
Visit: http://localhost:9001

Login: minioadmin / minioadmin

Create a bucket named: product-images

View Swagger UI
Open: http://localhost:8080/swagger-ui/index.html
.requestMatchers(
    "/swagger-ui/**",
    "/v3/api-docs/**",
    "/swagger-resources/**",
    "/webjars/**"
).permitAll()

Flyway Setup
src/main/resources/db/migration

Validation

Uses jakarta.validation annotations in DTO classes like @NotBlank, @NotNull.






