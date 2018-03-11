# sensor-hub
Web app for sensor data collection and representation in Spring Boot + Bootstrap 4 . 
It is prepared to be deployed in Heroku with a PostgreSQL database.

You need to initialize some Java properties if running locally from maven (they are already included in netbeans configuration): 
mvn spring-boot:run -Drun.jvmArguments="-Ddemo-user-role=ADMIN -Dsign-up-enabled=1 -Dapp-url=http://localhost:8080"

In the application property file you can change the default embedded H2 database to a PostgreSQL database. 
Also you need to set up your gmail address and gmail application password for the email class to work.

![Diagram](sensor-hub-architecture.PNG)

Dependencies loaded:

**spring-boot-starter-web:**
Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container

**spring-boot-starter-data-rest:**
Starter for exposing Spring Data repositories over REST using Spring Data REST

**spring-boot-starter-data-jpa:**
Starter for using Spring Data JPA with Hibernate

**postgresql:**:
Provides PostgreSQL database support.

**h2:**:
Provides H2 database support.

**spring-boot-starter-security:**
To include Spring Security

**spring-boot-starter-mail:**
Java Mail and Spring Framework's email sending support

**spring-boot-starter-thymeleaf:**
Add support for Thymeleaf views in MVC web applications 

**spring-boot-starter-test:**
Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito

**spring-boot-devtools:**
Includes some tools for development like auto deploy when code changes
    
**Presentation layer:**
  * Bootstrap 4
  * jQuery
  * jQueryUI Datepicker 
