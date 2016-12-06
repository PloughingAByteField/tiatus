(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp', [
        'ngRoute',
        'pascalprecht.translate',
        'ngSanitize',
        'ngMessages',
        'ngCookies',
        'dndLists',
        'AlertService',
        'RaceService',
        'ClubService',
        'PositionService',
        'EventService',
        'EntryService',
        'PositionController',
        'EventController',
        'RaceController',
        'ClubController',
        'EntryController',
        'DrawController',
        'UserController',
        'HomeController'
    ]);
})();
