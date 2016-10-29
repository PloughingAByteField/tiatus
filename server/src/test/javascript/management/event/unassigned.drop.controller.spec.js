describe("src.test.javascript.management.event.unassigned.drop.controller.spec.js", function() {

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

    describe('eventDropOnUnassignedController', function(){

        var scope, deferred, raceService, eventAssignedService, eventUnassignedService, alertService;

        beforeEach(inject(function($rootScope, $controller, $q, _raceService_, _eventAssignedService_, _eventUnassignedService_, _alertService_, $httpBackend) {
            raceService = _raceService_;
            alertService = _alertService_;
            eventAssignedService = _eventAssignedService_;
            eventUnassignedService = _eventUnassignedService_;
            deferredSave = $q.defer();
            raceServiceQuery = $q.defer();
            eventAssignedServiceQuery = $q.defer();
            eventUnassignedServiceQuery = $q.defer();
            eventUnassignedServiceCreate = $q.defer();
            deferredRemove = $q.defer();
            spyOn(raceService, 'getCurrentRace').and.returnValue({id: 1});
            spyOn(eventAssignedService, 'unassignEvent').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventUnassignedService, 'getUnassigned').and.returnValue(eventUnassignedServiceQuery.promise);
            spyOn(eventUnassignedService, 'unassignEvent').and.returnValue();
            scope = $rootScope.$new();
            ctrl = $controller('eventDropOnUnassignedController', {$scope: scope, raceService: raceService, eventUnassignedService: eventUnassignedService, eventAssignedService : eventAssignedService});
            $httpBackend.whenGET('home/home.html').respond();
        }));

        function getUnassignedEvents() {
            eventUnassignedServiceQuery.resolve([
                {name: "Event 1", id: 1, weighted: false},
                {name: "Event 2", id: 2, weighted: false},
                {name: "Event 3", id: 3, weighted: false}
            ]);
            scope.$apply();
        };

        it('should get unassigned events', function() {
            expect(ctrl.unassigned).toBeUndefined();
            expect(eventUnassignedService.getUnassigned).toHaveBeenCalled();
            getUnassignedEvents();

            expect(ctrl.unassigned.length).toBe(3);
        });

        it('should drop unassigned event', function() {
            getUnassignedEvents();
            expect(alertService.getAlert().msg).toBe(null);
            var result = ctrl.dropOnUnassigned('', 0, {});
            expect(result).toBe(false);
            expect(alertService.getAlert().msg).toBe(null);
        });

        it('should drop assigned event', function() {
            getUnassignedEvents();
            expect(alertService.getAlert().msg).toBe(null);
            var result = ctrl.dropOnUnassigned('', 0, {event: {name: 'name'}, raceEventOrder: 1});
            eventAssignedServiceQuery.resolve();
            scope.$apply();
            expect(result).toBe(true);
            expect(alertService.getAlert().msg).toBe(null);
        });

        it('should drop assigned event but fail unassign', function() {
            getUnassignedEvents();
            expect(alertService.getAlert().msg).toBe(null);
            var result = ctrl.dropOnUnassigned('', 0, {event: {name: 'name'}, raceEventOrder: 1});
            eventAssignedServiceQuery.reject();
            scope.$apply();
            expect(result).toBe(true);
            expect(alertService.getAlert().msg).toBe('FAILED_UNASSIGN');
        });

    });
});