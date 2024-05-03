# cs_task_24_04

Java (Spring) REST API with CRUD operations for `user` resource with validation, exception handling and testing.

## Table of Contents

- [Technologies](#technologies)
- [Installation](#installation)
- [Configuration](#configuration)

## Technologies

The project uses the following technologies and frameworks:

- Java
- Spring (Framework, Boot, MVC, Data JPA)
- Postgresql (as database)
- H2 (database for testing)
- Lombok (for boilerplate reduction)
- Maven (for project management)
- AssertJ (testing)
- Mockito (testing)
- Git (for version control)

## Installation

To run the project locally, follow these steps:

1. Clone the repository:

   ```
   git clone https://github.com/YaremaMaksym/cs_task_24_04.git
   ```

2. Open the project in your preferred IDE.

3. Set up the database:

   * Install and configure PostgreSQL on your system.
   * Create database 'test-user-db'
   * Update the `application.properties` file (see [Configuration](#configuration)) with your PostgreSQL credentials.

4. Run the application

The application should now be running on [http://localhost:8080](http://localhost:8080).

## Configuration
The project uses the `application.yml` file to configure the database connection and min age for registration. Here is an example of the file contents:

```
spring.application.name=cs_task_24_04

app.config.minAge=18

spring.datasource.url=jdbc:postgresql://localhost:5432/test-user-db
spring.datasource.username=postgres
spring.datasource.password=pass
spring.jpa.hibernate.ddl-auto=update


```
Make sure to replace `postgres` and `pass` with your actual PostgreSQL database credentials.
