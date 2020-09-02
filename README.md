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
|        |  tokenstoreservice.service
│        |  api-request.service
|        |  login-service
│
└──[mapreduce]
|  |  h2-1.4.200.jar
|  |  test.py
└──[twitter]
   |  build.gradle
   └──[src]
      └──[test]
      └──[main]
         └──[java/rest/twitter]
            |  CrosConfiger.java
            |  RedisCOnfig.java
            |  TwitterAplication.java
            |  Cronjob.java
            |  LoadData.java
            └──[controller] 
            └──[domain] 
            └──[security]
            └──[exeption]
            └──[repository]
         └──[resource]
            |  application.properties
```
## Build Frontend
If you clone the project and run the frontend part direclty, the command is
```bash
# navigate to angular-front-end folder, run below command, then access the web with URL: http://localhost:4042
ng serve
```
If you want to post it onto cloud end like AWS
```bash
# build it and post on AWS, you would see a dist folder created, then upload it to AWS
ng build -prod
```
## Build Backend
Here Gradle tool is used to build our backend part. There are two ways often used, one is use Graled pulgin in IDE like Intellij IDEA, another way is use command line in termial 
```bash
## navigate into the directory including build.gradle file 
gradle build
```
## Post on AWS
The project is posted by five parts seperately, including backend, database, redis, mapreduce, frontend. It also could be done with Docker, which uploads the whole project as one. First method is more convinent to modify some parts, but takes a little more time. 
Component    | AWS
---          | ---
Backend      | Elastic Beanstalk
Redis        | ElastiCache
DataBase     | RDS
MapReduce    | EC2
Frontend     | S3
## Screenshots
#### Registration
![Dashboard](/snapshot/registration.PNG?raw=true)
---
#### Login
![Dashboard](/snapshot/login.PNG?raw=true)
---
#### Home(spa)
![Dashboard](/snapshot/home.PNG?raw=true)
---
#### tweet
![Dashboard](/snapshot/tweet.PNG?raw=true)
##
