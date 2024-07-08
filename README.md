## Todo App 

This is a quick application build using Kotlin with Micronaut Framework and Angular v15 for the frontend.

It also utilizes Mysql as the database.


### Requirements:
- Todos consist of a title, category and an optional description
- Todos can be marked as done
- Todos can be displayed together or by category.
- Todos can be created.


### Framework 

Micronaut is a framework for building Microservices and Serverless Application utilizing either Java or Kotlin. 

The decision about utilizing it over other frameworks (e.g: Quarkus or Spring Boot), was given facility for initializing up a microservice. 

It has easy access to libraries, such JUNIT for testing, as well as it's own data libraries. 


### Database
For such a small project, most databases would have worked, both relational and non-relational. 
Thinking in scalability and the possible next steps that I envision to the project, `categories` would stop from been a `string` and rather be a relation from todo. Given this reason, I narrowed down the context between `MySQL` and `Postgres`. As both databases would offer good reliability, are open source and would offer similar performances, the chosen was made by time of experience utilizing each, and therefore `MySQL` was the choosen one. 

### Testing 
Both Frontend and Backend are covered by tests. Both uses `Fake Pattern` for external resources. 

The backend would still require to have extended coverage, including `Integration Tests` as the next step. 


### Next steps
- Adding Open API documentation
- Migrating JDBC from repository to a ORM
- Implement helper classes for the integration tests 
- Create dedicated docker setup for Integration tests 
- Dockerize application


### Running 

#### Pre requisites
- Docker 
- Java 21
- NodeJS 
 
#### Running
- Start the database with `docker-compose up` inside of the todo-backend folder
- Start the backend with `./gradlew run` nside of the todo-backend folder
- Start the frontend with `npm start`

#### Testing 
- Frontend tests can be run with `yarn test`
- Backend tests can be run with `./gradlew build`