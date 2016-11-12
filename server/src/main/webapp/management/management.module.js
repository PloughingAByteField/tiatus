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
        'PositionService',
        'EventService',
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
