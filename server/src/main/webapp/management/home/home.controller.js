(function() {
    'use strict'; // NOSONAR

    angular.module('HomeController', []).controller('homeController', HomeController);

    function HomeController($scope) {
        var vm = this;

        $scope.$parent.pageTitle = "Home";
    };

})();