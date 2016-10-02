(function() {
    'use strict'; // NOSONAR

    angular.module('loginApp').controller('appController', function($scope) {
        // create service for these
        $scope.titleImage = "/public/images/stopwatch.svg";
        $scope.favIcon = "/public/images/favicon.ico";
        $scope.eventTitle = "Tiatus";
    });
})();