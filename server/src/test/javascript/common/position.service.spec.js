describe("src.test.javascript.common.js.position.service.spec.js", function() {

    beforeEach(module('managementApp'));

    var mockResource, $httpBackend;
    beforeEach(inject(function ($injector) {
        $httpBackend = $injector.get('$httpBackend');
        mockResource = $injector.get('Position');
        $httpBackend.whenGET('lang/en.json').respond();
        $httpBackend.whenGET('lang/common/en.json').respond();
        $httpBackend.whenGET('lang/home/en.json').respond();
        $httpBackend.whenGET('home/home.html').respond();
    }));

    describe('call url', function() {

        it('should call url with get', function() {
            $httpBackend.expectGET('../rest/positions').respond([{id: 1, name: 'Position 1'}, {id: 2, name: 'Position 2'}]);

            var result = mockResource.query();
            $httpBackend.flush();

            expect(result[0].id).toEqual(1);
            expect(result[0].name).toEqual('Position 1');

            expect(result[1].id).toEqual(2);
            expect(result[1].name).toEqual('Position 2');
        });

        it('should call url with get active timing', function() {
            $httpBackend.expectGET('../rest/positions/activeTiming').respond([{id: 1, name: 'Position 1'}, {id: 2, name: 'Position 2'}]);

            var result = mockResource.getActiveTimingPositions();
            $httpBackend.flush();

            expect(result[0].id).toEqual(1);
            expect(result[0].name).toEqual('Position 1');

            expect(result[1].id).toEqual(2);
            expect(result[1].name).toEqual('Position 2');
        });

        it('should call url with delete', function() {
            $httpBackend.expectDELETE('../rest/positions/1').respond();

            mockResource.remove({ id: 1 });
            $httpBackend.flush();
        });

        it('should call with save returning 201', function() {
            $httpBackend.expectPOST('../rest/positions').respond(201, '', {'Location': '../rest/positions/2'});
            var result = mockResource.save({name: 'Position 1'});
            $httpBackend.flush();
        });

        it('should call with save returning 200', function() {
            $httpBackend.expectPOST('../rest/positions').respond({'id': 2});
            var result = mockResource.save({name: 'Position 1'});
            $httpBackend.flush();
        });

    });
});