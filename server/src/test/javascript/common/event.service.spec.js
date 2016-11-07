describe("src.test.javascript.common.js.event.service.spec.js", function() {

    beforeEach(module('managementApp'));

    var mockEvent, mockAssignedEvent, mockUnassignedEvent, $httpBackend;
    beforeEach(inject(function ($injector) {
        $httpBackend = $injector.get('$httpBackend');
        mockEvent = $injector.get('Event');
        mockAssignedEvent = $injector.get('AssignedEvent');
        mockUnassignedEvent = $injector.get('UnassignedEvent');
        $httpBackend.whenGET('lang/en.json').respond();
        $httpBackend.whenGET('lang/en.json').respond();
        $httpBackend.whenGET('lang/common/en.json').respond();
        $httpBackend.whenGET('lang/home/en.json').respond();
        $httpBackend.whenGET('home/home.html').respond();
    }));

    describe('call url', function() {

        it('should call url with get', function() {
            $httpBackend.expectGET('../rest/events/assigned').respond([
                {id: 1, race: {id: 1, name: 'Race 1'}, event: {name: "Event 1", id: 1, weighted: false}, raceEventOrder:1},
                {id: 2, race: {id: 2, name: 'Race 2'}, event: {name: "Event 2", id: 2, weighted: false}, raceEventOrder:1},
                {id: 3, race: {id: 1, name: 'Race 1'}, event: {name: "Event 3", id: 3, weighted: false}, raceEventOrder:2}
            ]);

            var result = mockAssignedEvent.query();
            $httpBackend.flush();

            expect(result[0].id).toEqual(1);
            expect(result[0].race.name).toEqual('Race 1');
            expect(result[0].event.name).toEqual('Event 1');

            expect(result[1].id).toEqual(2);
            expect(result[1].race.name).toEqual('Race 2');
            expect(result[1].event.name).toEqual('Event 2');

            expect(result[2].id).toEqual(3);
            expect(result[2].race.name).toEqual('Race 1');
            expect(result[2].event.name).toEqual('Event 3');
        });

        it('should call url with delete', function() {
            $httpBackend.expectDELETE('../rest/events/assigned/1').respond();

            mockAssignedEvent.remove({ id: 1 });
            $httpBackend.flush();
        });

        it('should call Event url with save returning 201', function() {
            $httpBackend.expectPOST('../rest/events').respond(201, '', {'Location': '../rest/events/2'});
            var result = mockEvent.save({name: 'Race 1'});
            $httpBackend.flush();
        });

        it('should call Event url with save returning 200', function() {
            $httpBackend.expectPOST('../rest/events').respond({'id': 2});
            var result = mockEvent.save({name: 'Race 1'});
            $httpBackend.flush();
        });

        it('should call AssignedEvent url with save returning 201', function() {
            $httpBackend.expectPOST('../rest/events/assigned').respond(201, '', {'Location': '../rest/events/assigned/2'});
            var result = mockAssignedEvent.save({name: 'Race 1'});
            $httpBackend.flush();
        });

        it('should call AssignedEvent url with save returning 200', function() {
            $httpBackend.expectPOST('../rest/events/assigned').respond({'id' : 2});
            var result = mockAssignedEvent.save({name: 'Race 1'});
            $httpBackend.flush();
        });
    });
});