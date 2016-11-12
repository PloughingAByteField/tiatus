(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp').controller('appController', function($scope) {

        $scope.titleImage = "/public/images/stopwatch.svg";
        $scope.favIcon = "/public/images/favicon.ico";
        $scope.eventTitle = "Tiatus";
        $scope.links = [
            {title: 'home', href: '#/'},
            {title: 'race', href: '#/race'},
            {title: 'position', href: '#/position'},
            {title: 'event', href: '#/event'},
            {title: 'club', href: '#/club'},
            {title: 'entry', href: '#/entry'},
            {title: 'draw', href: '#/draw'},
            {title: 'user', href: '#/user'},
            {title: 'logout', href: '/rest/logout'}
        ];
    });
})();