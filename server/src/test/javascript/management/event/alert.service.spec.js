describe("src.test.javascript.management.event.alert.service.spec.js", function() {

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

    describe('alert', function(){

        var scope, deferred, alertService;

        beforeEach(inject(function($rootScope, $controller, _alertService_, $httpBackend) {
            alertService = _alertService_;
            scope = $rootScope.$new();
            ctrl = $controller('eventController', {$scope: scope});
            $httpBackend.whenGET('home/home.html').respond();
        }));

        it('should get alert', function() {
            expect(alertService.getAlert().msg).toBe(null);
            expect(alertService.getAlert().type).toBe(null);
        });

        it('should set alert', function() {
            expect(alertService.getAlert().msg).toBe(null);
            expect(alertService.getAlert().type).toBe(null);
            alertService.setAlert("msg");
            expect(alertService.getAlert().msg).toBe("msg");
            expect(alertService.getAlert().type).toBe('danger');
        });

        it('should clear alert', function() {
            expect(alertService.getAlert().msg).toBe(null);
            expect(alertService.getAlert().type).toBe(null);
            alertService.setAlert("msg");
            expect(alertService.getAlert().msg).toBe("msg");
            expect(alertService.getAlert().type).toBe('danger');
            alertService.clearAlert();
            expect(alertService.getAlert().msg).toBe(null);
            expect(alertService.getAlert().type).toBe(null);
        });
    });
});