# 0.0.1 (29-07-2016)

## Features
- **This release is completion of the project scaffolding** with
    - multi module maven layout
    - CDI
    - JPA with in memory db integration test 
    - JAX-RS endpoint with unit test for for endpoints and annotations on the endpoints 
    - dummy javascript used to test the unit (karma) and integration (protractor) testing with coverage
    - Docker to deploy and run application for ui integration testing (selenium docker image)
    - Sonar test coverage enhancement with jacoco 
       
## To Do

Actual application

Documentation      
     
## Build Details
     
Code is hosted on github

Code is pushed to local gerrit instance.

Which pushes code to jenkins to do a Verifed build.

Code then gets a review and is merged to master on gerrit server which then replicates to github.