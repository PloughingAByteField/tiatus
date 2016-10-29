describe("src.test.javascript.management.event.assigned.drop.controller.spec.js", function() {

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

    describe('eventDropOnAssignedController', function(){

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
            spyOn(eventAssignedService, 'getAssignedEvents').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventAssignedService, 'reassignEvent').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventAssignedService, 'assignEvent').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventAssignedService, 'getAssignedEventsForRace').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventUnassignedService, 'getUnassigned').and.returnValue(eventUnassignedServiceQuery.promise);
            spyOn(eventUnassignedService, 'assignEvent').and.returnValue(eventUnassignedServiceQuery.promise);
            scope = $rootScope.$new();
            ctrl = $controller('eventDropOnAssignedController', {$scope: scope, raceService: raceService, eventUnassignedService: eventUnassignedService, eventAssignedService : eventAssignedService});
            $httpBackend.whenGET('home/home.html').respond();
        }));

        function getAssignedEvents() {
            eventAssignedServiceQuery.resolve([
                {id: 1, race: {id: 1, name: 'Race 1'}, event: {name: "Event 1", id: 1, weighted: false}, raceEventOrder:1},
                {id: 2, race: {id: 2, name: 'Race 2'}, event: {name: "Event 2", id: 2, weighted: false}, raceEventOrder:1},
                {id: 3, race: {id: 1, name: 'Race 1'}, event: {name: "Event 3", id: 3, weighted: false}, raceEventOrder:2}
            ]);
            scope.$apply();
        };

        it('should get assigned events', function() {
            expect(ctrl.assigned).toBeUndefined();
            expect(eventAssignedService.getAssignedEvents).toHaveBeenCalled();
            getAssignedEvents();

            expect(ctrl.assigned.length).toBe(3);
        });

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
            ctrl.dropOnAssigned('', 0, {});
            eventAssignedServiceQuery.resolve();
            eventUnassignedServiceQuery.resolve();
            scope.$apply();
            expect(eventAssignedService.assignEvent).toHaveBeenCalled();
            expect(eventAssignedService.reassignEvent).not.toHaveBeenCalled();
            expect(eventUnassignedService.assignEvent).toHaveBeenCalled();
            expect(alertService.getAlert().msg).toBe(null);
        });

        it('should drop unassigned event but fail assign', function() {
            getUnassignedEvents();
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.dropOnAssigned('', 0, {});
            eventAssignedServiceQuery.reject();
            scope.$apply();
            expect(eventAssignedService.assignEvent).toHaveBeenCalled();
            expect(eventUnassignedService.assignEvent).not.toHaveBeenCalled();
            expect(alertService.getAlert().msg).toBe('FAILED_ASSIGN');
        });

        it('should drop assigned event', function() {
            getUnassignedEvents();
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.dropOnAssigned('', 0, {raceEventOrder: 1});
            eventAssignedServiceQuery.resolve();
            scope.$apply();
            expect(eventAssignedService.reassignEvent).toHaveBeenCalled();
            expect(eventAssignedService.assignEvent).not.toHaveBeenCalled();
            expect(eventUnassignedService.assignEvent).not.toHaveBeenCalled();
            expect(alertService.getAlert().msg).toBe(null);
        });

        it('should drop assigned event but fail reassign', function() {
            getUnassignedEvents();
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.dropOnAssigned('', 0, {raceEventOrder: 1});
            eventAssignedServiceQuery.reject();
            scope.$apply();
            expect(eventAssignedService.reassignEvent).toHaveBeenCalled();
            expect(eventUnassignedService.assignEvent).not.toHaveBeenCalled();
            expect(alertService.getAlert().msg).toBe('FAILED_ASSIGN');
        });
    });
});