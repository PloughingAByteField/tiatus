(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp').config(function($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: 'lang/',
            suffix: '.json'
        });
        $translateProvider.useLocalStorage();
        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
        $translateProvider.useSanitizeValueStrategy('sanitize');
    });

    // configure our routes
    angular.module('managementApp').config(function($routeProvider) {
    		$routeProvider
    			.when('/', {
    				templateUrl : 'mgmt.html'
//                    resolve: {
//                        specificTranslations: function($translatePartialLoader, $translate) {
//                            $translatePartialLoader.addPart('common');
//                            $translatePartialLoader.addPart('management');
//                            return $translate.refresh();
//                        }
//                    }
    			})

    			.when('/race', {
    				templateUrl : 'race/race.html'
//                    resolve: {
//                        specificTranslations: function($translatePartialLoader, $translate) {
//                            $translatePartialLoader.addPart('common');
//                            $translatePartialLoader.addPart('race');
//                            return $translate.refresh();
//                        }
//                    }
    			})

            .otherwise({redirectTo: '/'});
    });

    angular.module('managementApp').controller('appController', function($scope, $rootScope) {
    // create service for these
        $scope.pageTitle = "hello there";
        $scope.titleImage = "/public/images/stopwatch.svg";
        $scope.favIcon = "/public/images/favicon.ico";
//        $scope.favIcon = "/tiatus/config/images/favicon.ico";
        $scope.eventTitle = "Tiatus";
        $scope.links = [{title: 'home', href: '#/'}, {title: 'race', href: '#/race'}, {title: 'logout', href: '/rest/logout'}]
    });
})();