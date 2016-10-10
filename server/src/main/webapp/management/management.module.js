(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp', [
        'ngRoute',
        'pascalprecht.translate',
        'ngSanitize',
        'ngMessages',
        'ngCookies',
        'RaceService',
        'EventService',
        'EventController',
        'RaceController',
        'HomeController'
    ]);
})();
