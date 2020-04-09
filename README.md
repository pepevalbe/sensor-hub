# sensor-hub
Web app for sensor data collection and representation in Spring Boot + Bootstrap 4. 
It is prepared to be deployed in Heroku with a MongoDB database. 
There are four application.properties files: One for development (configuration hardcoded in the file) and three for Heroku environments (configuration taken from config vars).

For development environment you can use the embedded MongoDB database activating the maven profile or use a local MongoDB 
instance setting uri in the property file. 
You need to set up your email address and password for the email sender class to work.

How to run: *spring-boot:run -P embedded-mongodb -Dspring-boot.run.profiles=development*

Arquitecture:
![Diagram](sensor-hub-architecture.PNG)

Dependencies loaded:

**spring-boot-starter-web:**
Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container

**spring-boot-starter-data-mongodb:**
Provides MongoDB database support.

**de.flapdoodle.embed.mongo:**
Provides embeded MongoDB database support for development environment.

**spring-boot-starter-security:**
To include Spring Security

**spring-boot-starter-mail:**
Java Mail and Spring Framework's email sending support

**spring-boot-starter-thymeleaf:**
Add support for Thymeleaf views in MVC web applications 

**spring-boot-starter-test:**
Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito

**lombok:**
Boilerplate code generator. Avoid repetitive code such as constructors, loggers getters and setters

**mapstruct:**
Mapping objects code generator used to pass objects between layers.
    
**Presentation layer:**
  * Bootstrap 4
  * Bootstrap Toggle
  * jQuery
  * jQueryUI Datepicker
  * Chart.js
  * Moment.js