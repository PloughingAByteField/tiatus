describe("src.test.javascript.management.event.controller.spec.js", function() {

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

    describe('eventController', function(){

        var scope, deferred, raceService, positionService, eventAssignedService, eventUnassignedService, alertService;

        beforeEach(inject(function($rootScope, $controller, $q, _raceService_, _eventAssignedService_, _eventUnassignedService_, _positionService_, _alertService_, $httpBackend) {
            raceService = _raceService_;
            alertService = _alertService_;
            positionService = _positionService_;
            eventAssignedService = _eventAssignedService_;
            eventUnassignedService = _eventUnassignedService_;
            deferredSave = $q.defer();
            raceServiceQuery = $q.defer();
            positionServiceQuery = $q.defer();
            eventAssignedServiceQuery = $q.defer();
            eventUnassignedServiceQuery = $q.defer();
            eventUnassignedServiceCreate = $q.defer();
            deferredRemove = $q.defer();
            spyOn(raceService, 'getRaces').and.returnValue(raceServiceQuery.promise);
            spyOn(positionService, 'getPositions').and.returnValue(positionServiceQuery.promise);
            spyOn(eventAssignedService, 'getAssignedEvents').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventAssignedService, 'getAssignedEventsForRace').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(eventUnassignedService, 'getUnassigned').and.returnValue(eventUnassignedServiceQuery.promise);
            spyOn(eventUnassignedService, 'createUnassignedEvent').and.returnValue(eventUnassignedServiceCreate.promise);
            spyOn(eventUnassignedService, 'removeUnassigned').and.returnValue(eventUnassignedServiceCreate.promise);
            spyOn(alertService, 'clearAlert').and.returnValue();
            scope = $rootScope.$new();
            ctrl = $controller('eventController', {$scope: scope, raceService: raceService, positionService: positionService, eventUnassignedService: eventUnassignedService, eventAssignedService : eventAssignedService});
            $httpBackend.whenGET('home/home.html').respond();
        }));

        function getRaces() {
            raceServiceQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
            scope.$apply();
        };

        it('should get races', function() {
            expect(ctrl.races).toBeUndefined();
            expect(raceService.getRaces).toHaveBeenCalled();
            getRaces();
            expect(ctrl.races.length).toBe(2);
            expect(ctrl.currentRace.id).toBe(1);
            expect(ctrl.currentRace.name).toBe('Race 1');
        });

        function getPositions() {
           positionServiceQuery.resolve([{"name":"Start", "id":1}, {"name":"Dangan", "id":2}, {"name":"Finish", "id":3}]);
           scope.$apply();
        };

        it('should get positions', function() {
            expect(ctrl.positions).toBeUndefined();
            expect(positionService.getPositions).toHaveBeenCalled();
            getPositions();
            expect(ctrl.positions.length).toBe(3);
        });

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

        it('should do initial race change', function() {
            getRaces();
            getAssignedEvents();
            expect(eventAssignedService.getAssignedEventsForRace).toHaveBeenCalled();
            eventAssignedServiceQuery.resolve([
                {id: 1, race: {id: 1, name: 'Race 1'}, event: {name: "Event 1", id: 1, weighted: false}, raceEventOrder:1},
                {id: 3, race: {id: 1, name: 'Race 1'}, event: {name: "Event 3", id: 3, weighted: false}, raceEventOrder:2}
            ]);
            scope.$apply();
            expect(raceService.getCurrentRace().id).toBe(1);
            expect(raceService.getCurrentRace().name).toBe('Race 1');
        });

        it('should call clearAlert', function() {
            ctrl.closeAlert();
            expect(alertService.clearAlert).toHaveBeenCalled();
        });

        it('should call create event', function() {
            ctrl.event = {data: 'data', weighted: true};
            ctrl.createEvent(ctrl.event);
            ctrl.addEventForm = {
                $setPristine: function() {},
                $setUntouched: function() {}
            };

            expect(eventUnassignedService.createUnassignedEvent).toHaveBeenCalled();
            eventUnassignedServiceCreate.resolve();
            scope.$apply();
            expect(ctrl.event.data).toBeUndefined();
            expect(ctrl.event.weighted).toBe(false);
        });

        it('should call create event but throw error', function() {
            ctrl.event = {data: 'data', weighted: true};
            ctrl.createEvent(ctrl.event);
            expect(alertService.getAlert().msg).toBe(null);
            expect(eventUnassignedService.createUnassignedEvent).toHaveBeenCalled();
            eventUnassignedServiceCreate.reject();
            scope.$apply();
            expect(ctrl.event.data).toBe('data');
            expect(ctrl.event.weighted).toBe(true);
            expect(ctrl.event.weighted).toBe(true);
            expect(alertService.getAlert().msg).toBe('FAILED_ADD');
        });

        it('should call delete event', function() {
            ctrl.deleteEvent(1);
            expect(eventUnassignedService.removeUnassigned).toHaveBeenCalled();
            eventUnassignedServiceCreate.resolve();
            scope.$apply();
        });

        it('should call delete event but throw error', function() {
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.deleteEvent(1);
            expect(eventUnassignedService.removeUnassigned).toHaveBeenCalled();
            eventUnassignedServiceCreate.reject();
            scope.$apply();
            expect(alertService.getAlert().msg).toBe('FAILED_REMOVE');
        });
    });
});