(function() {
    'use strict'; // NOSONAR

    angular.module('managementApp').config(function($translateProvider) {
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'lang/{part}/{lang}.json'
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
    				templateUrl : 'home/home.html',
    				title: 'HOME_TITLE',
                    resolve: {
                        specificTranslations: function($translatePartialLoader, $translate) {
                            $translatePartialLoader.addPart('common');
                            $translatePartialLoader.addPart('home');
                            return $translate.refresh();
                        }
                    }
    			})

    			.when('/race', {
    				templateUrl : 'race/race.html',
    				title: 'RACES_TITLE',
                    resolve: {
                        specificTranslations: function($translatePartialLoader, $translate) {
                            $translatePartialLoader.addPart('common');
                            $translatePartialLoader.addPart('race');
                            return $translate.refresh();
                        }
                    }
    			})

            .otherwise({redirectTo: '/'});
    });

    angular.module('managementApp').run(['$rootScope', '$route', '$translate', function($rootScope, $route, $translate) {
        $rootScope.$on('$routeChangeSuccess', function() {
            $translate($route.current.title).then(function (title) {
                document.title = title ;
            });
        });
    }]);
})();