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
                expect($route.routes['/position'].templateUrl).toEqual('position/position.html');
                expect($route.routes['/club'].templateUrl).toEqual('club/club.html');
                expect($route.routes['/event'].templateUrl).toEqual('event/event.html');
                expect($route.routes['/entry'].templateUrl).toEqual('entry/entry.html');
                expect($route.routes['/draw'].templateUrl).toEqual('draw/draw.html');
                expect($route.routes['/user'].templateUrl).toEqual('user/user.html');
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

        it('should switch to position', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('position/position.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/position');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('position/position.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('position')).toBe(true);
            });
        });

        it('should switch to club', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('club/club.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/club');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('club/club.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('club')).toBe(true);
            });
        });

        it('should switch to entry', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('entry/entry.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/entry');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('entry/entry.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('entry')).toBe(true);
            });
        });

        it('should switch to draw', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('draw/draw.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/draw');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('draw/draw.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('draw')).toBe(true);
            });
        });

        it('should switch to user', function() {
            inject(function($route, $location, $rootScope, $translatePartialLoader, $httpBackend) {
                $httpBackend.expectGET('user/user.html').respond();
                expect($route.current).toBeUndefined();
                $location.path('/user');
                $rootScope.$digest();
                expect($route.current.templateUrl).toBe('user/user.html');
                expect($translatePartialLoader.isPartAvailable('common')).toBe(true);
                expect($translatePartialLoader.isPartAvailable('user')).toBe(true);
            });
        });
    });
});