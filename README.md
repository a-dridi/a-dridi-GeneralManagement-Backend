# General Management - Microservices Backend - REST API in Java Spring

The backend of the application general management. An application to manage expenses, earnings, books and more.
The backend is programmed in Java and uses Spring Boot. It has a microservices architecture and uses eureka to connect the different services (modules). 
Zuul is used to create the gateway which will be the public access to the other services. 
All java modules and server are created as executable jar files and contain an embedded tomcat server. These jar files can be deployed as Linux systemd services.

## Demo Video of the [Frontend](https://github.com/a-dridi/GeneralManagement-Frontend) (English, Intl. version):
[https://youtu.be/Bk1MI7UNzeM](https://youtu.be/Bk1MI7UNzeM)

## Demo Video of the [Frontend](https://github.com/a-dridi/GeneralManagement-Frontend)(Deutsche Version):
[https://youtu.be/5Q54V4c5rg0](https://youtu.be/5Q54V4c5rg0)

## Microservices Architecture

These different points are seperate Java modules. The Eureka Server is the top and all others are clients. 

### app-server (Eureka Server)
Name: gm-server
Description: The server for the whole microservices application. All other services connect to this server.

### gm-gateway (Zuul Gateway)
Name: gm-gateway-service
Database: generalmanagement
Description: Main public entry point. All API requests are done on this application and port. - User Management (Registration, Login etc. is done here).


### common (Eureka Client)
Name: common-service
Database: gm_common
Description: Notes for all tables, Application settings, other saved tables data that do not belong to a certain data table group (e.g.: Books, Organization , Trade).


### budgeting (Eureka Client)
Name: budgeting-service
Database gm_budgeting
Description: Expenses, earnings and their sub tables. 

### media (Eureka Client)
Name: media-service
Database gm_media
Description: Videos, Videoclips, Music, Software and their sub tables. 

### financial (Eureka Client)
Name: financial-service
Database gm_financial
Description: Wealth, crypto currencies, reserves and their sub tables. 
 

## API

### Public API end points
These API end points are available without authentication:

/api/login/

You can login or register a new user account (called owner) which is used to authenticate. All data is, then saved for that user. The user can access and edit only his data.  

**IMPORTANT:** You need to add a user, to use the backend and frontend of the application "General Management". 

### Registration of a user
POST /api/registration

Add your email, password, forename and surname in a JSON format to your POST body, by using this format:
```
{
     "email": "myemail@MYEMAIL.tld",
     "username": "MY_USERNAME",
     "password": "MY_PASSWORD",
     "admin": true,
     "roles": "ADMIN",
     "permissions": ""
}
```

### Login of a user
POST /api/login

Add your email and password in a JSON format to your POST body. 


### Secured API end points
You need to authenticate with a user account (owner) as shown above. You have to add your JWT token to your header the following to the header key parameter "Authorization":
Bearer YOUR_JWT_TOKEN_HASH_STRING_XXXXXXXXXX

Id stands as a place holder for a real id. 

### Get an Expense - GET:
/api/budgeting/data/expense/get/byId/[ID] 

Example: 

GET /api/budgeting/data/expense/get/byId/2


### Save an Expense - POST:
/api/budgeting/data/expense/add

Example: 
POST /api/budgeting/data/expense/add

Add expense values in a JSON format in your body. 
Example of the JSON body content: 
```
{
      title: "Title",
      expenseCategory: {},
      centValue: 45,
      expenseTimerange: {},
      paymentDate: "Mon, 25 Dec 199513:30:00 GMT",
      information: "Info",
      attachment: true,
      attachmentPath: "",
      attachmentName: "",
      attachmentType: "",
      deleted: false,
      userId: 105
}
```

All API directives are in the URI: /api/[SERVICE-NAME]/data


## Build
Build the application in an IDE. WAR file will be created in the folder "target" in the project root folder.


## Run
If you want to start the application in an IDE, then you need to have Lombok installed in your IDE.


## Used technologies
Java 14, Spring, Spring Boot, Lombok, Eureka, Zuul, JWT & PostgreSQL

## Authors

* **A. Dridi** - [a-dridi](https://github.com/a-dridi/)