# sdt-simple-cashier-web

<p>
This Project is SoftwareDevelopersToday Project of CMS Dashboard Web for User Management and Internal Application. 
Running on Java version 11 and Spring Boot 2.7.5 platform. Using Spring Security for Basic Auth, PostgreSQL for RDBMS, Bootstrap 3 and Jquery as the Front End. 
Admin Dashboard Template refers to Startmin template (You can refer to their original documentation on Github, under MIT Licence).
</p>
<p>
## License

This project uses the Startmin template, which is licensed under the [MIT License](https://github.com/secondtruth/startmin/blob/v1.1/LICENSE).

### MIT License Summary:
Startmin Template © [2013 – 2023] [Christian Neff & Start Bootstrap LLC.]

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

https://github.com/secondtruth/startmin

</p>
<p>
    <label>This CMS Dashboard Web is a simple cashier web for small Point of Sale (POS)</label>
</p>

## Getting Started

* Database Configuration

<p>
To run this project, make sure you have already run PostgreSQL Driver on your local or docker container, as the existing driver runs on http://localhost:5432/shops.
If the database has not already been exist, create the database on that PostgreSQL (refer to src/main/resources/application-dev.yml).
Configure your username and password on src/main/resources/application-dev.yml based on the database name in PostgreSQL

```
CREATE USER sdt_service WITH PASSWORD 'SDTService123'
CREATE DATABASE shops OWNER sdt_service;
```
The FlywayDB will automatically create schemas and tables after application running. 
</p>

* Clone this repository from Github 

<p>
Clone this repository on Terminal by Https:

```
$ git clone https://github.com/ariobimo74/sdt-simple-cashier.git
```
</p>

* Run The Application
<p>
To run the application, you have to make sure that Java Runtime and Compiler minimum version 11 is running on your local. Check both Java Version :

Java Runtime Environment:
```
$ java -version
```
Java Compiler :
```
$ javac -version
```

To run the application using maven, you have to make sure maven is running on your local, unless you run the application using IDE. 
To check the maven, type the command on terminal:
```
$ mvn -version
```
Run the application using command:
```
$ mvn spring-boot:run
```
The application will run in http://localhost:8082 (src/main/resources/application.yml)
<br/>
username for login : tester
<br/>
password : tester
</p>

## Bugs and Issues
<p>

Bugs and Issues are open to be discussed on [Issues](https://github.com/ariobimo74/sdt-simple-cashier/issues).
</p>
