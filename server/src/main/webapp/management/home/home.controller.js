(function() {
    'use strict'; // NOSONAR

    angular.module('HomeController', []).controller('homeController', HomeController);

    function HomeController($scope) {
        $scope.$parent.pageTitle = "Home";
    };

})();