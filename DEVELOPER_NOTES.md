# Developer Notes

This is a java maven project with an angular frontend using docker for end to end testing.

A very high level diagram of the system is ![overview](../docs/tiatus_network_arch.png)  

Java 8 and maven 3.3.x is required to build and run unit tests. 

If running the application locally, a PostgreSQL server and Wildlfy are required. I use jrebel to speed up development by avoiding containter redeployments but it is not a requirement. 

To run integration tests, a docker local install is required. The images used can be quite big so a broadband connection is recommended.

The images created are:

A PostgreSQL image is created with a populated user and database for the application image to use.

A Wildfly image with the application pre installed and configured.

A selemium server image containing some browsers used by the protractor e2e testing tool to test the application.

There is a maven dev profile. Select it in your IDE or include it when running maven on the command line.

Flyway is used to create the database contents as part of the dev profile. Its configuration options are in flyway/flyway.properties.  
To skip it running against the database use mvn -Dflyway.skip=true package   
To clean the database mvn -Pdev flyway:clean   


To run the e2e tests locally via protactor a local install of selenium in the server directory
./node_modules/grunt-protractor-runner/scripts/webdriver-manager-update   
and to start it   
webdriver-manager start


With the local wildfly instance running on port 8080, run   
grunt protractor --baseUrl=http://127.0.0.1:8080

Maven site is a bit screwed up by maven 3.  To generate javadoc do
mvn -Ddocker.skip=true -Dflyway.skip=true -Dmaven.test.skip=true install javadoc:aggregate
The doc will be in target/site/apidocs

