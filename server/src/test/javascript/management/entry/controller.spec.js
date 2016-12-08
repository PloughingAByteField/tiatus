describe("src.test.javascript.management.entry.controller.spec.js", function() {

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

    describe('entryController', function(){

        var scope, deferred, entryService, alertService, raceService, eventService, eventAssignedService, clubService, Entry, route, rootScope;

        beforeEach(inject(function($rootScope, $route, $controller, $q, _entryService_, _raceService_, _eventService_, _eventAssignedService_, _clubService_, _AssignedEvent_, _Entry_, _alertService_, $httpBackend) {
            Entry = _Entry_;
            route = $route;
            rootScope = $rootScope;
            entryService = _entryService_;
            raceService = _raceService_;
            eventService = _eventService_;
            eventAssignedService = _eventAssignedService_;
            clubService = _clubService_;
            AssignedEvent = _AssignedEvent_;
            alertService = _alertService_;
            raceServiceQuery = $q.defer();
            eventServiceQuery = $q.defer();
            eventAssignedServiceQuery = $q.defer();
            clubServiceQuery = $q.defer();
            entryServiceSave = $q.defer();
            entryServiceQuery = $q.defer();
            entryServiceUpdate = $q.defer();
            entryServiceRemove = $q.defer();
            assignedEventSave = $q.defer();
            entrySave = $q.defer();
            spyOn(raceService, 'getRaces').and.returnValue(raceServiceQuery.promise);
            spyOn(eventService, 'getEvents').and.returnValue(eventServiceQuery.promise);
            spyOn(eventAssignedService, 'getAssignedEvents').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(AssignedEvent, 'save').and.returnValue({ $promise: assignedEventSave.promise });
            spyOn(eventAssignedService, 'getAssignedEventsForRace').and.returnValue(eventAssignedServiceQuery.promise);
            spyOn(clubService, 'getClubs').and.returnValue(clubServiceQuery.promise);
            spyOn(entryService, 'getEntries').and.returnValue(entryServiceQuery.promise);
            spyOn(Entry, 'save').and.returnValue({ $promise: entrySave.promise });
            spyOn(Entry, 'remove').and.returnValue({ $promise: entrySave.promise });
            spyOn(alertService, 'clearAlert').and.returnValue();
            scope = $rootScope.$new();
            ctrl = $controller('entryController', {$scope: scope, entryService: entryService, alertService: alertService});
            $httpBackend.whenGET('home/home.html').respond();
            $httpBackend.whenGET('entry/entry_details.html').respond();
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

        function getEvents() {
            eventServiceQuery.resolve([
                {event: {name: "Event 1", id: 1, weighted: false}},
                {event: {name: "Event 2", id: 2, weighted: false}},
                {event: {name: "Event 3", id: 3, weighted: false}}
            ]);
            scope.$apply();
        };

        it('should get events', function() {
            expect(ctrl.events).toBeUndefined();
            expect(eventService.getEvents).toHaveBeenCalled();
            getEvents();

            expect(ctrl.events.length).toBe(3);
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

        function getClubs() {
            clubServiceQuery.resolve([{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}]);
            scope.$apply();
        };

        it('should get clubs', function() {
            expect(ctrl.clubs).toBeUndefined();
            getClubs();
            expect(clubService.getClubs).toHaveBeenCalled();
            expect(ctrl.clubs.length).toBe(2);
        });

        function getEntries() {
            entryServiceQuery.resolve([
                {id: 1, clubs: [{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}], event: {name: 'Event 1', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false},
                {id: 2, clubs: [{id: 2, clubName: 'Club 3'}], event: {name: 'Event 2', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false}
            ]);
            scope.$apply();
        };

        it('should get entries', function() {
            expect(ctrl.entries).toBeUndefined();
            getEntries();
            expect(entryService.getEntries).toHaveBeenCalled();
            expect(ctrl.entries.length).toBe(2);
        });

        it('should get entries but fail', function() {
            expect(ctrl.entries).toBeUndefined();
            entryServiceQuery.reject();
            scope.$apply();
            expect(entryService.getEntries).toHaveBeenCalled();
            expect(ctrl.entries).toBeUndefined();
        });

        it('should be data ready', function() {
            getEntries();
            getAssignedEvents();
            getRaces();
            scope.$apply();
        });

        it('should trigger addEventChangeListener callback', function() {
            getAssignedEvents();
            eventAssignedService.assignEvent(0, {id: 1}, 0);
            assignedEventSave.resolve({id:2});
            scope.$apply();
        });

        it('should trigger addEntryChangeListener callback', function() {
            entrySave.resolve({id:2});
            entryService.addEntry({});

            scope.$apply();
        });

        it('should call clearAlert', function() {
            ctrl.closeAlert();
            expect(alertService.clearAlert).toHaveBeenCalled();
        });

        it('should redirect to details on add entry', function() {
            ctrl.newEntry();
            rootScope.$digest();
            expect(route.current.templateUrl).toBe('entry/entry_details.html');
            expect(entryService.getActiveEntry().id).toBeUndefined();
        });

        it('should redirect to details on update entry', function() {
            ctrl.updateEntry({id: 1});
            rootScope.$digest();
            expect(route.current.templateUrl).toBe('entry/entry_details.html');
            expect(entryService.getActiveEntry().id).toBe(1);
        });

        it('should remove entry', function() {
            getEntries();
            expect(ctrl.entries.length).toBe(2);
            ctrl.removeEntry(ctrl.entries[0]);
            entrySave.resolve();
            scope.$apply();
        });

        it('should attempt to remove entry but fail', function() {
            getEntries();
            expect(ctrl.entries.length).toBe(2);
            ctrl.removeEntry(ctrl.entries[0]);
            entrySave.reject();
            scope.$apply();
        });
    });
});