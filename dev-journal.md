# Development Journal

## Overview
This journal documents the progress, decisions, and key milestones achieved during the development of the **Blog API Project**.
  


---

## Reflections and Next Steps
The project has transitioned from basic CRUD operations to a secure, scalable API with JWT authentication and role-based access control.

My plan was to create an API to provide endpoints for managing blog entries. The API would be used by a blog website implemented with React. My plan for deployment was github pages for the front end app and AWS for the backend API and database. I was not sure which AWS product to use in the beginning. My first idea was to use EC2 containers with RDS for database for the lowest cost, but ultimately I went with AWS lightsail to ensure a predictible monthly cost.

I used udemy courses to guide me along during the creation of key spring boot features. These guides were especially helpful.

[[NEW] Spring Boot 3, Spring 6 & Hibernate for Beginners](https://www.udemy.com/course/spring-hibernate-tutorial/?kw=%5BNEW%5D+Spring+Boot+3%2C+Spring+6+%26+Hibernate+for+Beginners&src=sac&subs_filter_type=subs_only) - Used for Spring Data JPA 

[The Complete Spring Boot Development Bootcamp](https://www.udemy.com/course/the-complete-spring-boot-development-bootcamp/?kw=The+Complete+Spring+Boot+Development+Bootcamp&src=sac&subs_filter_type=subs_only) - Used for Spring Security and setting up JWT authentication

I used chat GPT as a coding assistant. I found the AI responses helpful for general questions about Spring Boot, Java, Docker, Linux, and AWS. The AI responses were often incorrect or outdated when it came to specific implementations. Sometimes the AI did help with giving me ideas on how to fix a problem I was having, but more often debugging and stack trace analysis were what helped me resolve the issue myself. 

- Enhancements:
  - **Improve documentation and test coverage.**
  - **Changelog and version control.** This hasn't been set up yet as the first functional production ready version of the API has only just been completed.
  - **Enhance validation and error handling for user input.** Username input into username/password needs more validation.
  - **Optimize database interactions for scalability.** (database indexing)
  - **Research security enhancements.** Consider further if CSRF protections would increase security. Right now I don't think so.
  - **Research circular dependency issue.** I resolved a circular dependency issue by using @Lazy loading on some spring injected dependencies. I'd like to look further into this as there may be some code refactoring needed.
  - **More local development imporovements.** Add to MySQL docker container start up script so that the database is initialized with the appropriate tables and test data.
  - **Impove deployment process.** Deploying the API to lightsail container is a bit tedius. I could improve the process by writing a script to package my project, build a tagged docker image, and deploy it to the AWS container registry.
  - **API documentation.** Implement Swagger to generate automatically updated API definition from code with examples.
  - **Make** Use make to automate repetitive development tasks"

- Future features:
  - **Web sockets.** I'd like to do something with web sockets so I can learn.
  - **


---

### Notes
This journal will be updated regularly to track progress and highlight key milestones.

---