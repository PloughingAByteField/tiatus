describe("src.test.javascript.management.event.service.js", function() {

    beforeEach(module('managementApp'));

    describe('eventService', function() {
        var Event, eventService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _Event_, _eventService_, $q) {
            Event = _Event_;
            eventService = _eventService_;
            deferredUpdate = $q.defer();
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(Event, 'query').and.returnValue({$promise: deferredQuery.promise});
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();
        }));

        function getEvents() {
            var events;
            eventService.getEvents().then(function(data) {
                events = data;
            });
            expect(Event.query).toHaveBeenCalled();
            deferredQuery.resolve([
              {id: 1, clubs: [{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}], event: {name: 'Event 1', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false},
              {id: 2, clubs: [{id: 2, clubName: 'Club 3'}], event: {name: 'Event 2', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false}
            ]);
            scope.$apply();
            return events;
        };

        it('should get events', function() {
           var events = getEvents();
           expect(events.length).toBe(2);
        });

        it('should attempt to get events but fail', function() {
            eventService.getEvents();
            deferredQuery.reject();
            scope.$apply();
            expect(Event.query).toHaveBeenCalled();
        });


    });
});