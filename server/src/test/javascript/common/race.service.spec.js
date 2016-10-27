describe("src.test.javascript.common.js.race.service.spec.js", function() {

    beforeEach(module('managementApp'));

    var mockResource, $httpBackend;
    beforeEach(inject(function ($injector) {
        $httpBackend = $injector.get('$httpBackend');
        mockResource = $injector.get('Race');
        $httpBackend.whenGET('lang/en.json').respond();
        $httpBackend.whenGET('lang/common/en.json').respond();
        $httpBackend.whenGET('lang/home/en.json').respond();
        $httpBackend.whenGET('home/home.html').respond();
    }));

    describe('call url', function() {

        it('should call url with get', function() {
            $httpBackend.expectGET('../rest/races').respond([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);

            var result = mockResource.query();
            $httpBackend.flush();

            expect(result[0].id).toEqual(1);
            expect(result[0].name).toEqual('Race 1');

            expect(result[1].id).toEqual(2);
            expect(result[1].name).toEqual('Race 2');
        });

        it('should call url with delete', function() {
            $httpBackend.expectDELETE('../rest/races/1').respond();

            mockResource.remove({ id: 1 });
            $httpBackend.flush();
        });

        it('should call url with save', function() {
            $httpBackend.expectPOST('../rest/races').respond({id: 1, name: 'Race 1'});

            var result = mockResource.save({name: 'Race 1'});
            $httpBackend.flush();
            expect(result.id).toEqual(1);
            expect(result.name).toEqual('Race 1');
        });
    });
});