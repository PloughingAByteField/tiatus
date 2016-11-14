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

                eventAssignedService.assignEvent({id: 4}, {id: 1}, 2);
                expect(AssignedEvent.save).toHaveBeenCalled();
                expect(AssignedEvent.update).not.toHaveBeenCalled();
                deferredSave.resolve({id: 4});
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(3);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(3);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].events[2].event.id).toBe(4);
                expect(assignedEvents[0].events[2].raceEventOrder).toBe(3);
                expect(assignedEvents[0].events[2].id).toBe(4);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should assign event dropped on empty', function() {
                var assignedEvents;
                eventAssignedService.getAssignedEvents().then(function(data) {
                    assignedEvents = data;
                });
                expect(AssignedEvent.query).toHaveBeenCalled();
                deferredQuery.resolve([]);
                scope.$apply();
                expect(assignedEvents.length).toBe(0);

                eventAssignedService.assignEvent({id: 4}, {id: 1}, 0);
                deferredSave.resolve({id: 4});
                scope.$apply();

                expect(AssignedEvent.update).not.toHaveBeenCalled();
                expect(AssignedEvent.save).toHaveBeenCalled();

                expect(assignedEvents.length).toBe(1);
                expect(assignedEvents[0].events.length).toBe(1);
                expect(assignedEvents[0].events[0].id).toBe(4);
                expect(assignedEvents[0].events[0].event.id).toBe(4);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].race.id).toBe(1);
            });

            it('should assign event dropped at start', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, {id: 1}, 0);
                deferredUpdate.resolve();
                scope.$apply()

                deferredSave.resolve({id: 4});
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();
                expect(AssignedEvent.save).toHaveBeenCalled();

                expect(assignedEvents[0].events.length).toBe(3);
                expect(assignedEvents[0].events[0].id).toBe(4);
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

                eventAssignedService.assignEvent({id: 4}, {id: 1}, 1);
                deferredUpdate.resolve();
                scope.$apply()

                deferredSave.resolve({id: 4});
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();
                expect(AssignedEvent.save).toHaveBeenCalled();


                expect(assignedEvents[0].events.length).toBe(3);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(4);
                expect(assignedEvents[0].events[1].id).toBe(4);
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

                eventAssignedService.assignEvent({id: 4}, {id: 1}, 1);
                deferredUpdate.resolve();
                scope.$apply();

                deferredSave.reject();
                scope.$apply();

                expect(AssignedEvent.update).toHaveBeenCalled();
                expect(AssignedEvent.save).toHaveBeenCalled();

                // if save fails then the data is corrupt but can be fixed thorugh the ui
                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(3);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(3);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should fail to assign event due to update failure', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.assignEvent({id: 4}, {id: 1}, 1);
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.reject();
                scope.$apply();
                expect(AssignedEvent.save).not.toHaveBeenCalled();

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

            it('should unassign event', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.unassignEvent({event: {id: 1}}, {id: 1});
                expect(AssignedEvent.remove).toHaveBeenCalled();
                deferredRemove.resolve();
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(1);
                expect(assignedEvents[0].events[0].event.id).toBe(3);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should unassign event with no updates', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.unassignEvent({event: {id: 3}}, {id: 1});
                expect(AssignedEvent.remove).toHaveBeenCalled();
                deferredRemove.resolve();
                scope.$apply();
                expect(AssignedEvent.update).not.toHaveBeenCalled();

                expect(assignedEvents[0].events.length).toBe(1);
                expect(assignedEvents[0].events[0].event.id).toBe(1);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should fail to unassign event when removing', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.unassignEvent({ event: {id: 1}}, {id: 1});
                expect(AssignedEvent.remove).toHaveBeenCalled();
                deferredRemove.reject();
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

            it('should fail to unassign event when updating', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.unassignEvent({ event: {id: 1}}, {id: 1});
                expect(AssignedEvent.remove).toHaveBeenCalled();
                deferredRemove.resolve();
                scope.$apply();
                deferredUpdate.reject();
                scope.$apply();
                expect(AssignedEvent.update).toHaveBeenCalled();

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

            it('should reassign event before', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.reassignEvent({ event: {id: 3}}, {id: 1}, 0);
                expect(AssignedEvent.save).not.toHaveBeenCalled();
                expect(AssignedEvent.remove).not.toHaveBeenCalled();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(3);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(1);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should reassign event after', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.reassignEvent({ event: {id: 1}}, {id: 1}, 1);
                expect(AssignedEvent.save).not.toHaveBeenCalled();
                expect(AssignedEvent.remove).not.toHaveBeenCalled();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(3);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(1);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should reassign event at end', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.reassignEvent({ event: {id: 1}}, {id: 1}, 2);
                expect(AssignedEvent.save).not.toHaveBeenCalled();
                expect(AssignedEvent.remove).not.toHaveBeenCalled();
                expect(AssignedEvent.update).toHaveBeenCalled();
                deferredUpdate.resolve();
                scope.$apply();

                expect(assignedEvents[0].events.length).toBe(2);
                expect(assignedEvents[0].events[0].event.id).toBe(3);
                expect(assignedEvents[0].events[0].raceEventOrder).toBe(1);
                expect(assignedEvents[0].events[1].event.id).toBe(1);
                expect(assignedEvents[0].events[1].raceEventOrder).toBe(2);
                expect(assignedEvents[0].race.id).toBe(1);
                expect(assignedEvents[1].events.length).toBe(1);
                expect(assignedEvents[1].race.id).toBe(2);
                expect(assignedEvents[1].events[0].event.id).toBe(2);
            });

            it('should not reassign event if drop on self', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.reassignEvent({ event: {id: 1}}, {id: 1}, 0);
                expect(AssignedEvent.save).not.toHaveBeenCalled();
                expect(AssignedEvent.remove).not.toHaveBeenCalled();
                expect(AssignedEvent.update).not.toHaveBeenCalled();
                deferredUpdate.resolve();
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

            it('should not reassign event if update fails', function() {
                var assignedEvents = getAssignedEvents();
                expect(assignedEvents.length).toBe(2);

                eventAssignedService.reassignEvent({ event: {id: 1}}, {id: 1}, 1);
                expect(AssignedEvent.save).not.toHaveBeenCalled();
                expect(AssignedEvent.remove).not.toHaveBeenCalled();
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

            it('should return race assigned events', function() {
                var assignedEvents = getAssignedEvents();
                var data = eventAssignedService.getAssignedEventsForRace({id: 1});
                expect(data.length).toBe(2);
            });

            it('should return empty race assigned events', function() {
                var assignedEvents = getAssignedEvents();
                var data = eventAssignedService.getAssignedEventsForRace({id: 4});
                expect(data.length).toBe(0);
            });
        });
    });
});