(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp').controller('appController', function($scope) {
    // create service for these
        $scope.titleImage = "/public/images/stopwatch.svg";
        $scope.favIcon = "/public/images/favicon.ico";
//        $scope.favIcon = "/tiatus/config/images/favicon.ico";
        $scope.eventTitle = "Tiatus";
        $scope.links = [{title: 'home', href: '#/'}, {title: 'race', href: '#/race'}, {title: 'logout', href: '/rest/logout'}]
    });
})();