## Angular Frontent + SpringBoot (Java) Backend
This is mini Twitter project including frontend and backend. Frontend is based on Agular while backend is REST service based on SrpingBoot. The function includes registration, login, authentication and authorization, tweet post and delete, comment, like, follow, hot tweets, cache CRUD, CORS access, and new tweet notification. 

### Technology Stack
Component         | Technology
---               | ---
Frontend          | [Angular](https://angular.io/)
Backend (REST)    | [SpringBoot](https://projects.spring.io/spring-boot) 
Data access       | [JPA](https://spring.io/projects/spring-data-jpa)
Security          | [Spring Security and JWT](https://spring.io/projects/spring-securit)
DB                | MySQL
In Memory DB      | H2 
Cache             | [Redis](https://spring.io/projects/spring-data-redis)
Mapreduce         | [Appache Beam](https://beam.apache.org/)
Cron job          | Spring Scheduled
Cloud             | AWS
Client Build Tools| angular-cli, npm
Server Build Tools| Gradle

## Folder Structure
```bash
PROJECT_FOLDER
└──[angular-front-end] 
|  |  README.md
|  |  package.json 
│  └──[src]      
│     |  inde.html 
│     └──[app]
│        └──[register]
│        └──[login]
│        └──[user]
│        └──[tweets]
│        └──[follower]
│        └──[profile]
│        |  api-request.service
|        |  login-service
│
└──[mapreduce]
|  |  h2-1.4.200.jar
|  |  test.py
└──[twitter]
|  |  build.gradle
|  └──[src]
|     └──[test]
|     └──[main]
|        └──[java/rest/twitter]
|        └──[resource]
|           └──[controller] 
|           └──[domain] 
|           └──[security]
|           └──[exeption]
|           └──[repository]
|           |  CrosConfiger.java
|           |  RedisCOnfig.java
|           |  TwitterAplication.java
```
