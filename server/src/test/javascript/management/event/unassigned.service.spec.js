describe("src.test.javascript.management.event.unassigned.service.js", function() {

    beforeEach(module('managementApp'));

    describe('eventUnassignedService', function() {
        var Event, eventUnassignedService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _Event_, _UnassignedEvent_, _eventUnassignedService_, $q) {
            UnassignedEvent = _UnassignedEvent_;
            Event = _Event_;
            deferredQuery = $q.defer();
            deferredSave = $q.defer();
            deferredRemove = $q.defer();
            spyOn(UnassignedEvent, 'query').and.returnValue({$promise: deferredQuery.promise});
            spyOn(Event, 'save').and.returnValue({ $promise: deferredSave.promise });
            spyOn(Event, 'remove').and.returnValue({ $promise: deferredRemove.promise });
            eventUnassignedService = _eventUnassignedService_;
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();

        }));

        function getUnassignedEvents() {
            var unassignedEvents;
            eventUnassignedService.getUnassigned().then(function(data) {
                unassignedEvents = data;
            });
            expect(UnassignedEvent.query).toHaveBeenCalled();
            deferredQuery.resolve([
                {name: "Event 1", id: 1, weighted: false},
                {name: "Event 2", id: 2, weighted: false},
                {name: "Event 3", id: 3, weighted: false}
            ]);
            scope.$apply();
            return unassignedEvents;
        };

        describe('unassigned events', function() {
            it('should fetch assigned events', function() {
                var unassignedEvents = getUnassignedEvents();
                expect(unassignedEvents.length).toBe(3);
                expect(unassignedEvents[0].id).toBe(1);
                expect(unassignedEvents[1].id).toBe(2);
                expect(unassignedEvents[2].id).toBe(3);
            });

            it('should fail to fetch assigned events', function() {
                var unassignedEvents;
                eventUnassignedService.getUnassigned().then(function(data) {
                    unassignedEvents = data;
                });
                expect(UnassignedEvent.query).toHaveBeenCalled();
                deferredQuery.reject();
                scope.$apply();
                expect(unassignedEvents).toBeUndefined();
            });

            it('assigning should remove from list', function() {
                var unassignedEvents = getUnassignedEvents();
                eventUnassignedService.assignEvent({id: 2});
                expect(unassignedEvents.length).toBe(2);
                expect(unassignedEvents[0].id).toBe(1);
                expect(unassignedEvents[1].id).toBe(3);
            });

            it('remove should remove from list', function() {
                var unassignedEvents = getUnassignedEvents();
                eventUnassignedService.removeUnassigned({id: 2});
                expect(Event.remove).toHaveBeenCalled();
                deferredRemove.resolve();
                scope.$apply();
                expect(unassignedEvents.length).toBe(2);
                expect(unassignedEvents[0].id).toBe(1);
                expect(unassignedEvents[1].id).toBe(3);
            });

            it('remove should not remove from list on failure', function() {
                var unassignedEvents = getUnassignedEvents();
                eventUnassignedService.removeUnassigned({id: 2});
                expect(Event.remove).toHaveBeenCalled();
                deferredRemove.reject();
                scope.$apply();
                expect(unassignedEvents.length).toBe(3);
                expect(unassignedEvents[0].id).toBe(1);
                expect(unassignedEvents[1].id).toBe(2);
                expect(unassignedEvents[2].id).toBe(3);
            });

            it('createUnassignedEvent should add to list', function() {
                var unassignedEvents = getUnassignedEvents();
                eventUnassignedService.createUnassignedEvent({name: 'Event new'});
                expect(Event.save).toHaveBeenCalled();
                deferredSave.resolve({id: 4});
                scope.$apply();
                expect(unassignedEvents.length).toBe(4);
                expect(unassignedEvents[0].id).toBe(1);
                expect(unassignedEvents[1].id).toBe(2);
                expect(unassignedEvents[2].id).toBe(3);
                expect(unassignedEvents[3].id).toBe(4);
                expect(unassignedEvents[3].name).toEqual('Event new');
            });

            it('createUnassignedEvent should not add to list if failure', function() {
                var unassignedEvents = getUnassignedEvents();
                eventUnassignedService.createUnassignedEvent({name: 'Event new'});
                expect(Event.save).toHaveBeenCalled();
                deferredSave.reject();
                scope.$apply();
                expect(unassignedEvents.length).toBe(3);
                expect(unassignedEvents[0].id).toBe(1);
                expect(unassignedEvents[1].id).toBe(2);
                expect(unassignedEvents[2].id).toBe(3);
            });

            it('unassignedEvent at start should add to list and shift others', function() {
                var unassignedEvents = getUnassignedEvents();
                eventUnassignedService.unassignEvent({id: 4, name: 'Event new'}, 0);
                expect(Event.save).not.toHaveBeenCalled();
                expect(unassignedEvents.length).toBe(4);
                expect(unassignedEvents[0].id).toBe(4);
                expect(unassignedEvents[0].name).toEqual('Event new');
                expect(unassignedEvents[1].id).toBe(1);
                expect(unassignedEvents[2].id).toBe(2);
                expect(unassignedEvents[3].id).toBe(3);
            });
        });
    });
});