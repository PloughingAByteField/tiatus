(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp', [
        'ngRoute',
        'pascalprecht.translate',
        'ngSanitize',
        'ngMessages',
        'ngCookies',
        'dndLists',
        'RaceService',
        'EventService',
        'EventController',
        'RaceController',
        'HomeController'
    ]);
})();
