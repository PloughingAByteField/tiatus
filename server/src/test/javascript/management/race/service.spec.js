describe("src.test.javascript.management.race.service.js", function() {

    beforeEach(module('managementApp'));

    describe('raceService', function() {
        var Race, raceService, deferred;

        beforeEach(inject(function ($rootScope, $httpBackend, _Race_, _raceService_, $q) {
            Race = _Race_;
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(Race, 'query').and.returnValue({$promise: deferredQuery.promise});
            spyOn(Race, 'save').and.returnValue({ $promise: deferredSave.promise });
            spyOn(Race, 'remove').and.returnValue({ $promise: deferredRemove.promise });
            raceService = _raceService_;
            $httpBackend.whenGET().respond();
            scope = $rootScope.$new();

        }));

        describe('getCurrentRace', function(){
            it('should fetch empty object', function() {
                var current = raceService.getCurrentRace();
                expect(current).toEqual({});
            });

            it('should set current race', function() {
                raceService.setCurrentRace(1);
                var current = raceService.getCurrentRace();
                expect(current).toBe(1);
            });
        });

        function getRaces() {
            var races;
            raceService.getRaces().then(function(data) {
                races = data;
            });
            expect(Race.query).toHaveBeenCalled();
            deferredQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
            scope.$apply();
            return races;
        };

        describe('data', function() {
            it('should fetch races', function() {
                var races = getRaces();
                expect(races.length).toBe(2);
            });

            it('should fail to fetch races', function() {
                var races;
                raceService.getRaces().then(function(data) {
                    races = data;
                });
                expect(Race.query).toHaveBeenCalled();
                deferredQuery.reject();
                scope.$apply();
                expect(races).toBeUndefined();
            });

            it('should add race', function() {
                var races = getRaces();
                expect(races.length).toBe(2);

                raceService.addRace({name: 'Race 1'}).then(function(data) {});
                expect(Race.save).toHaveBeenCalled();
                deferredSave.resolve({id: 1, name: 'Race 1'});
                scope.$apply();
                expect(races.length).toBe(3);
            });

            it('should fail to add race', function() {
                var races = getRaces();
                expect(races.length).toBe(2);

                raceService.addRace({name: 'Race 1'}).then(function(data) {});
                expect(Race.save).toHaveBeenCalled();
                deferredSave.reject();
                scope.$apply();
                expect(races.length).toBe(2);
            });

            it('should remove race', function() {
                var races = getRaces();
                expect(races.length).toBe(2);

                raceService.removeRace({name: 'Race 1'}).then(function(data) {});
                expect(Race.remove).toHaveBeenCalled();
                deferredRemove.resolve();
                scope.$apply();
                expect(races.length).toBe(1);
            });

            it('should fail to remove race', function() {
                var races = getRaces();
                expect(races.length).toBe(2);

                raceService.removeRace({name: 'Race 1'}).then(function(data) {});
                expect(Race.remove).toHaveBeenCalled();
                deferredRemove.reject();
                scope.$apply();
                expect(races.length).toBe(2);
            });
        });
    });
});