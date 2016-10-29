describe("src.test.javascript.management.config.spec.js", function() {

    beforeEach(module(function($logProvider) { // test fail other wise
        // We can configure the debugging level (the default is true)
        $logProvider.debugEnabled(false);
    }));

    beforeEach(module('managementApp', function ($provide, $translateProvider) {

        $provide.factory('customLoader', function ($q) {
            return function () {
                var deferred = $q.defer();
                deferred.resolve({});
                return deferred.promise;
            };
        });

        $translateProvider.useLoader('customLoader');
    }));

    describe('route check', function() {

        it('should map routes to controllers', function() {
            inject(function($route) {
                expect($route.routes['/'].templateUrl).toEqual('home/home.html');
                expect($route.routes['/race'].templateUrl).toEqual('race/race.html');
                expect($route.routes['/event'].templateUrl).toEqual('event/event.html');
                expect($route.routes[null].redirectTo).toEqual('/')
            });
        });

        it('should switch to home', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('home/home.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('home/home.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('home')).toBe(true);
            });
        });

        it('should switch to race', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('race/race.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/race');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('race/race.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('race')).toBe(true);
            });
        });

        it('should switch to event', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('event/event.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/event');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('event/event.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('event')).toBe(true);
            });
        });
    });
});