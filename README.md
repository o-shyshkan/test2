#**user-aggregating-app**
user-aggregating-app it is application with REST API designed for aggregating users data from multiple databases.
This service has declarative configuration for specification of data sources. Maximal quantity
of data sources are infinite. When need to add/remove new datasource only one yaml configuration file is required to
be edited.

##**Features**:
1. Add/Remove databases from [application.yml](src%2Fmain%2Fresources%2Fapplication.yml) file;
2. Get all users from all databases;
3. Get all users by user name;
4. Get and sent information in JSON format;
5. Docker-compose configuration for run postgreSQL with pgadmin for test application

##**Structure:**
* test2 - root directory for project;
* config - configuration for datasource and JdbcTemplate bean
* controller - package for Spring controllers where defined end point;
* mapper - package for converting model to DTO
* model - package for model User and UserResponseDto;
* service - package for service interfaces;
* yaml - package for getting YAML and DataBaseProperties
* resources - package for databases and docker configuration;

##**Technology:**
1. Java - JDK 17
2. Database - PostgreSQL 11
3. Spring Boot framework - 3.1.5
4. Docker 20.10.24
5. Docker Compose version v2.17.2

##**Getting started:**
1. Make new directory for user-aggregating-app application, for example test2
2. In command line clone code from repository to test2 directory
   ```https://github.com/o-shyshkan/test2```
3. Open user-aggregating-app project in your favorite IDE
4. Set up connection to your databases in file [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
5. Also instead use own database you can start prepared postgreSQL and pgadmin container by type in command line docker-compose up  
   And use default configuration of datasource in [application.yml](src%2Fmain%2Fresources%2Fapplication.yml)
6. Run program
7. To get all users from databases type url http://localhost:8080/users
8. There are two endpoints available that show below

##**Description REST API of user-aggregating-app:**
1. Get all Users from databases

   GET http://localhost:8080/users

   Response:
   [{"id":"1",
   "username":"admin",
   "name":"Admin",
   "surname":"Adminov"},
   {"id":"2",
   "username":"alex",
   "name":"Alexander",
   "surname":"A"}]
2. Get User by username

   GET http://localhost:8080/users/by-username?username=admin

   Response:
   [{"id":"1",
   "username":"admin",
   "name":"Admin",
   "surname":"Adminov"}]