# TODO

1. unit tests (will wait till e2e tests have been done as refactoring will probably be done at the same time)
    * most of the java code has tests
    * the original AngularJS karma tests are now dust, all the Angular codebase will require tests

2. integration (e2e) tests (In Progress)
    * most of the java db and dao layers have integration tests
    * the original AngularJS protractor tests are now dust, all the Angular codebase will require tests, there may be a lite test suite run as part of the build and seperate fuller real event sized suites.

3. offline support 
    * the original AngularJS code for timer position detected when a user went offline while using the page and wrote updates to local storage with background sync. This has a number of issues, mainly had to be online to start the app.
    * Want to use service worker for offline support, app will start even when offline. May use IndexDB 2 and flux (Redux) state mangement. I expect webkit will add service worker support in due time.

4. ~~caching~~ (Done)
    * ~~the original AngularJS results had data written to file so ETag would be used by browser and polled every 30 secs.~~ Results polls every 30 seconds.
    * ~~Now the data is rest based and an ETag cache layer is required at the rest layer to prevent hitting the db layer if there are no changes~~. Wildfly infispan cache is used to store ETag cache of rest calls at the rest layer, this may be refactored later to happen as a rest intecptor.
    * The volume of data has been reduced by JPA and JSON changes to pass sub entity ids instead of full entites.

5. ~~ws~~ (Done)
    * ~~the original AngularJS used websockets to send messages between timing positions and adjuicators. It also was used to send administrative updates, event creation, new entries, etc to online timing positions/adjudicators to update their data. This functionality is missing.~~

6. configuration
    * There was no configuraton support in the original AngularJS codebase, this has been added to the new Angular codebase but is very basic at best and needs improvment.

7. documentation
    * there is none, maybe gitbook 

8. new features
    * In the new codebase, timing points have been added to the event instead of the inflexible fixed positions per race from before. This could be extended to add laps support without major changes.
    * line support and heat to final progression support is something that does not fit into the data model used very easily and would have to be a long term feature wish.