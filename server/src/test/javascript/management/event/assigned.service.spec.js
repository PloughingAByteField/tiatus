describe("src.test.javascript.management.event.assigned.service.js", function() {

    beforeEach(module('managementApp'));

    describe('eventAssignedService', function() {
        var Race, eventAssignedService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _AssignedEvent_, _eventAssignedService_, $q) {
            AssignedEvent = _AssignedEvent_;
            deferredUpdate = $q.defer();
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(AssignedEvent, 'query').and.returnValue({$promise: deferredQuery.promise});
            spyOn(AssignedEvent, 'update').and.returnValue({$promise: deferredUpdate.promise});
            spyOn(AssignedEvent, 'save').and.returnValue({ $promise: deferredSave.promise });
            spyOn(AssignedEvent, 'remove').and.returnValue({ $promise: deferredRemove.promise });
            eventAssignedService = _eventAssignedService_;
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();

        }));

        function getAssignedEvents() {
            var assignedEvents;
            eventAssignedService.getAssignedEvents().then(function(data) {
                assignedEvents = data;
            });
            expect(AssignedEvent.query).toHaveBeenCalled();
            deferredQuery.resolve([
                {id: 1, race: {id: 1, name: 'Race 1'}, event: {name: "Event 1", id: 1, weighted: false}, raceEventOrder:1},
                {id: 2, race: {id: 2, name: 'Race 2'}, event: {name: "Event 2", id: 2, weighted: false}, raceEventOrder:1},
                {id: 3, race: {id: 1, name: 'Race 1'}, event: {name: "Event 3", id: 3, weighted: false}, raceEventOrder:2}
            ]);
            scope.$apply();
            return assignedEvents;
        };

        describe('assigned events', function() {
            it('should fetch assigned events', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);
                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(3);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
                expect(assignedEvents[1].events[0].raceEventOrder).toBe(1);
            });

            it('should fail to fetch assigned events', function() {
                var assignedEvents;
                eventAssignedService.getAssignedEvents().then(function(data) {
                    assignedEvents = data;
                });
                expect(AssignedEvent.query).toHaveBeenCalled();
                deferredQuery.reject();
                scope.$apply();
                expect(assignedEvents).toBeUndefined();
            });

            it('should assign event dropped at end', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, 1, 2);
                expect(AssignedEvent.save).toHaveBeenCalled();
                deferredSave.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(3);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(3);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].events[2].event.id).toBe(4);
                expect(assignedEvents[0].events[2].raceEventOrder).toBe(3);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should assign event dropped at start', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, 1, 0);
                expect(AssignedEvent.save).toHaveBeenCalled();
                deferredSave.resolve();
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(3);
                expect(assignedEvents[0].events[0].event.id).toBe(4);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(1);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].events[2].event.id).toBe(3);
                expect(assignedEvents[0].events[2].raceEventOrder).toBe(3);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should assign event dropped in middle', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, 1, 1);
                expect(AssignedEvent.save).toHaveBeenCalled();
                deferredSave.resolve();
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(3);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(4);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].events[2].event.id).toBe(3);
                expect(assignedEvents[0].events[2].raceEventOrder).toBe(3);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should fail to assign event due to save failure', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, 1, 1);
                expect(AssignedEvent.save).toHaveBeenCalled();
                deferredSave.reject();
                scope.$apply();

                expect(AssignedEvent.update).not.toHaveBeenCalled();

                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(3);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should fail to assign event due to update failure', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, 1, 1);
                expect(AssignedEvent.save).toHaveBeenCalled();
                deferredSave.resolve();
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.reject();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(3);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });
        });
    });
});