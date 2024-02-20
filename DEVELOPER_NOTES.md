# Developer Notes

This is a java maven Spring Boot 3.x project with an angular frontend using docker for end to end testing.

A very high level diagram of the system is ![overview](docs/tiatus_network_arch.png)  

Java 17 and maven 3.9.x is required to build and run unit tests. 

If running the application locally, a PostgreSQL server is required. This can be run through docker, the dockerfile is provided to create the image. 

To run integration tests, a docker local install is required. The images used can be quite big so a broadband connection is recommended.

The images created are:

A PostgreSQL image is created with a populated user and database for the application image to use.

A Wildfly image with the application pre installed and configured.

A selemium server image containing some browsers used by the protractor e2e testing tool to test the application.

There is a maven dev profile. Select it in your IDE or include it when running maven on the command line.

Flyway is used to create the database contents as part of the dev profile. Its configuration options are in flyway/flyway.properties.  
To skip it running against the database use mvn -Dflyway.skip=true package   
To clean the database mvn -Pdev flyway:clean
   
To wipe the db on a docker postgres instance, replacing IP_ADDRESS and the port as required. 
mvn -Pdev flyway:clean flyway:migrate -Dflyway.url=jdbc:postgresql://IP_ADDRESS:6432/tiatus

For now a forked version of boxable is being used, that will have to built locally, see the link in the server/pom.xml file.

To run the e2e tests locally via protactor a local install of selenium in the server directory

./node_modules/protractor/bin/webdriver-manager update   

and to start it   

./node_modules/protractor/bin/webdriver-manager start

With the local Spring Boot instance running on port 8080, run
   
./node_modules/protractor/bin/protractor webpack/protractor.conf.js --baseUrl=http://127.0.0.1:8080 --directConnect=true

Replace ip address and port if running against a docker wildfly instance. The directConnect is required as the protractor configuration is to connect to a selenium hub instance.

Maven site is a bit screwed up by maven 3.  To generate javadoc do
mvn -Ddocker.skip=true -Dflyway.skip=true -Dmaven.test.skip=true install javadoc:aggregate
The doc will be in target/site/apidocs

To keep docker instances running after verify has completed add the following 

mvn verify -Ddocker.not-stop-instances=true

This is useful for protractor runs.

