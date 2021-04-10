# Appointment Rest Api
Steps to start applications

Make sure to set database credentials on
* booking-service/src/main/resources/application.properties
* cleaner-service/src/main/resources/application.properties

Create two database schemas accordingly.(default schema names: booking, service )

Clone the repo to desired path and then run the following commands

<code>cd appointment/eureka-server</code>

<code>mvn spring-boot:run</code>


<code>cd ../gateway</code>


<code>mvn spring-boot:run</code>


<code>cd ../booking-service</code>


<code>mvn spring-boot:run</code>


<code>cd ../cleaner-service</code>


<code>mvn spring-boot:run</code>


Automatically created api docs:

http://localhost:6141/swagger-ui/

http://localhost:8554/swagger-ui/

I have also added Postman collection export if you want to try the rest apis on Postman.

Example get call to apis through gateway

`http://localhost:4522/api/v1/cleaner`

# Algorithm

You can call 
<code>php count_similarities.php</code>

then give the input in following format
```
2
aaa
ddaaddaa
```
