# Do It
[![NPM](https://img.shields.io/npm/l/react)](https://github.com/kaiqlopes/doit-api/blob/main/LICENSE)

# About the Project

Do It is a To Do List app which has the focus on helping users to be more organized, centering all their responsabilities in one place through task creations with a variety of customizations.

### Features:

- Register your tasks, choose its priority, categories, expiration dates, task status and give detailed description about what to do.
<br><br>
- You can share the task with others and remove them. If it is the case, you can also give or remove administrator permissions which grants the users more authority and flexibility over the tasks. 
<br><br>
- Users with permissions can invite new task users, give administrator rights, update or delete tasks.
<br><br>
- Account creation, authentication token using Oauth2 and JWT. Users that doesn't have an account are not able to use the app or be invited to any task.
<br><br>
- Some endpoints are protected, allowing only system admin accounts to access it.

## Domain Model
![doit2](https://github.com/kaiqlopes/doit-api/assets/58572272/806bc715-b362-4d52-855e-1dca6988334d)

## Used Technologies

- Java 21
- <a href = "https://spring.io/projects/spring-boot">Spring Boot<a/>
- <a href = "https://spring.io/projects/spring-data">Spring Data JPA<a/>
- <a href = "https://spring.io/projects/spring-security">Spring Security<a/>
- <a href = "https://oauth.net/2/">Oauth 2<a/> / <a href = "https://jwt.io/">JWT<a/>
- <a href = "https://beanvalidation.org/">Jakarta Bean Validation<a/>
- <a href = "https://www.h2database.com/html/main.html">H2 Database(Testing)<a/>
- <a href = "https://www.postgresql.org/">PostgreSQL<a/>
- <a href = "https://junit.org/junit5/">JUnit 5<a/>
- <a href = "https://maven.apache.org/">Maven<a/>

## Skills

- Layered Architecture <br>
![image](https://github.com/kaiqlopes/client-api/assets/58572272/79213efb-9599-4eb5-a492-cca895f8ed09)
  <br><br>
- Spring Security applied in order to limit some endpoints and generate tokens that expires within a day, tokens are necessary to access all endpoints and validate users
- Customized HTTP response codes using ResponseEntity
- Customized exceptions response messages and codes using exception handlers
- Customized Constraint Validator to validate user updates
- N+1 queries problems solved in order to improve performance
- Tests using Postman
- Unit and integration tests writen using JUnit 5 and Mockito ensuring application integrity

## How to Run

<b>Prerequisites:</b> Java 17
<br><br>

<b>1.</b> Clone the repository
  ```bash
  git clone git@github.com:kaiqlopes/doit-api.git
```
<br>

<b>2.</b> Open the project in your preferred IDE and execute it

<br>

<b>3.</b> Use any API REST testing tool


## Author
<b>_Kaique Lopes da Silva_</b> <br>
<div align="left" style="display: inline-block;">
  <a href="https://www.linkedin.com/in/kaiqlopes/" target="_blank">
    <img align= "center" alt="Kaiq-linkedin" = height "110" width="110" src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white">
  </a>
</div>
