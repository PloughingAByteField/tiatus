describe("src.test.javascript.common.js.club.service.spec.js", function() {

    beforeEach(module('managementApp'));

    var mockClub, $httpBackend;
    beforeEach(inject(function ($injector) {
        $httpBackend = $injector.get('$httpBackend');
        mockClub = $injector.get('Club');
        $httpBackend.whenGET('lang/en.json').respond();
        $httpBackend.whenGET('lang/common/en.json').respond();
        $httpBackend.whenGET('lang/home/en.json').respond();
        $httpBackend.whenGET('home/home.html').respond();
    }));

    describe('call url', function() {

        it('should call url with get', function() {
            $httpBackend.expectGET('../rest/clubs').respond([{id: 1, club: 'Club 1'}]);

            var result = mockClub.query();
            $httpBackend.flush();

            expect(result[0].id).toEqual(1);
            expect(result[0].club).toEqual('Club 1');
        });

        it('should call url with delete', function() {
            $httpBackend.expectDELETE('../rest/clubs/1').respond();

            mockClub.remove({ id: 1 });
            $httpBackend.flush();
        });

        it('should call Event url with save returning 201', function() {
            $httpBackend.expectPOST('../rest/clubs').respond(201, '', {'Location': '../rest/clubs/1'});
            var result = mockClub.save({name: 'Club 1'});
            $httpBackend.flush();
        });

        it('should call Event url with save returning 200', function() {
            $httpBackend.expectPOST('../rest/clubs').respond({'id': 1});
            var result = mockClub.save({name: 'Club 1'});
            $httpBackend.flush();
        });

    });
});