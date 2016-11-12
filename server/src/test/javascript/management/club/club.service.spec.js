describe("src.test.javascript.management.club.service.js", function() {

    beforeEach(module('managementApp'));

    describe('clubService', function() {
        var Club, clubService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _Club_, _clubService_, $q) {
            Club = _Club_;
            clubService = _clubService_;
            deferredUpdate = $q.defer();
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(Club, 'query').and.returnValue({$promise: deferredQuery.promise});
            spyOn(Club, 'update').and.returnValue({$promise: deferredUpdate.promise});
            spyOn(Club, 'save').and.returnValue({ $promise: deferredSave.promise });
            spyOn(Club, 'remove').and.returnValue({ $promise: deferredRemove.promise });
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();

        }));

        function getClubs() {
            var clubs;
            clubService.getClubs().then(function(data) {
                clubs = data;
            });
            expect(Club.query).toHaveBeenCalled();
            deferredQuery.resolve([{id: 1, club: 'Club 1'}, {id: 2, name: 'Club 2'}]);
            scope.$apply();
            return clubs;
        };

        describe('club service', function() {
            it('should get clubs', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);
            });

            it('should fail to fetch clubs', function() {
                var clubs;
                expect(clubService.clubs).toBeUndefined();
                clubService.getClubs().then(function(data){
                    clubs = data;
                });
                deferredQuery.reject();
                scope.$apply();
                expect(Club.query).toHaveBeenCalled();
                expect(clubs).toBeUndefined();
            });

            it('should add club', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);

                var club = {name: 'Club 3'};
                clubService.addClub(club);
                deferredSave.resolve({id: 3});
                scope.$apply();

                expect(Club.save).toHaveBeenCalled();

                clubs = getClubs()
                expect(clubs.length).toBe(3);
                expect(clubs[2].id).toBe(3);
                expect(clubs[2].name).toBe('Club 3');
            });

            it('should attempt to add club but fail', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);

                var club = {name: 'Club 3'};
                clubService.addClub(club);
                deferredSave.reject();
                scope.$apply();

                expect(Club.save).toHaveBeenCalled();

                 clubs = getClubs();
                 expect(clubs.length).toBe(2);
            });

            it('should remove club', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);

                var club = {id: 2, name: 'Club 2'};
                clubService.removeClub(club);
                deferredRemove.resolve();
                scope.$apply();

                expect(Club.remove).toHaveBeenCalled();

                clubs = getClubs()
                expect(clubs.length).toBe(1);
            });

            it('should attempt to remove club but fail', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);

                var club = {id: 2, name: 'Club 2'};
                clubService.removeClub(club);
                deferredRemove.reject();
                scope.$apply();

                expect(Club.remove).toHaveBeenCalled();

                clubs = getClubs()
                expect(clubs.length).toBe(2);
            });

            it('should update club', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);

                var club = {id: 2, name: 'Club 2a'};
                clubService.updateClub(club);
                deferredUpdate.resolve();
                scope.$apply();

                expect(Club.update).toHaveBeenCalled();

                clubs = getClubs()
                expect(clubs.length).toBe(2);
                expect(clubs[1].id).toBe(2);
                expect(clubs[1].name).toBe('Club 2a');
            });

            it('should attempt to update club but fail', function() {
                var clubs = getClubs();
                expect(clubs.length).toBe(2);

                var club = {id: 2, name: 'Club 2a'};
                clubService.updateClub(club);
                deferredUpdate.reject();
                scope.$apply();

                expect(Club.update).toHaveBeenCalled();

                clubs = getClubs()
                expect(clubs.length).toBe(2);
                expect(clubs[1].id).toBe(2);
                expect(clubs[1].name).toBe('Club 2');
            });
        });
    });
});