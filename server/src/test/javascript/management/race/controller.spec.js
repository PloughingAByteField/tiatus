describe("src.test.javascript.management.race.controller.spec.js", function() {

    beforeEach(module(function($logProvider) { // test fail other wise
        // We can configure the debugging level (the default is true)
        $logProvider.debugEnabled(false);
    }));

    beforeEach(module('managementApp', function ($provide, $translateProvider) {
    
      $provide.factory('customLoader', function ($q) {
        return function () {
          var deferred = $q.defer();
          deferred.resolve({});
          return deferred.promise;
        };
      });
    
      $translateProvider.useLoader('customLoader');
    }));

    describe('raceController', function(){

        describe('getRaces', function(){
            var scope, deferred, Race;

            beforeEach(inject(function($rootScope, $controller, $q, _Race_) {
                    Race = _Race_;
                    deferredSave = $q.defer();
                    deferredQuery = $q.defer();
                    deferredRemove = $q.defer();
                    spyOn(Race, 'query').and.returnValue({$promise: deferredQuery.promise});
                    spyOn(Race, 'save').and.returnValue({ $promise: deferredSave.promise });
                    spyOn(Race, 'remove').and.returnValue({ $promise: deferredRemove.promise });
                    scope = $rootScope.$new();
                    ctrl = $controller('raceController', {$scope: scope, Race: Race});
                    ctrl.races = [];
            }));


            it('should fetch 2 races from mocked backend', function() {
                expect(ctrl.races.length).toBe(0);
                deferredQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
                scope.$apply();
                expect(ctrl.races.length).toBe(2);

                expect(Race.save).not.toHaveBeenCalled();
                expect(Race.remove).not.toHaveBeenCalled();

                // have to use angular.equals as $resource tacks on a promise and .equals can figure the data from the promise
                expect(angular.equals(ctrl.races, [{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}])).toBe(true);
            });

            it('should fail to fetch races from mocked backend', function() {
                expect(ctrl.races.length).toBe(0);
                deferredQuery.reject();
                scope.$apply();
                expect(ctrl.races.length).toBe(0);
                expect(ctrl.alert).not.toBeUndefined();
                // as error message will be localised by angular-translate which has been mocked
                expect(ctrl.alert.msg).toBe("FAILED_FETCH");
                expect(ctrl.alert.type).toBe("danger");

                expect(Race.save).not.toHaveBeenCalled();
                expect(Race.remove).not.toHaveBeenCalled();
            });
        });

        describe('addRace', function(){
            var scope, deferred, Race;

            beforeEach(inject(function($rootScope, $controller, $q, _Race_) {
                    Race = _Race_;
                    deferredSave = $q.defer();
                    deferredQuery = $q.defer();
                    deferredRemove = $q.defer();
                    spyOn(Race, 'query').and.returnValue({$promise: deferredQuery.promise});
                    spyOn(Race, 'save').and.returnValue({ $promise: deferredSave.promise });
                    spyOn(Race, 'remove').and.returnValue({ $promise: deferredRemove.promise });
                    scope = $rootScope.$new();
                    ctrl = $controller('raceController', {$scope: scope, Race: Race});
                    ctrl.races = [];
            }));

            it('should add new race to races', function() {
                var race = {};
                race.name = 'Race 3';
                race.order = 3;

                ctrl.addRaceForm = {
                    $setPristine: function() {},
                    $setUntouched: function() {}
                }

                expect(ctrl.races.length).toBe(0);
                deferredQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
                scope.$apply();

                expect(ctrl.races.length).toBe(2);
                ctrl.addRace(race);
                deferredSave.resolve({id: 3, name: 'Race 3'});
                scope.$apply();

                expect(Race.save.calls.mostRecent().args.length).toBe(1);
                expect(Race.save.calls.mostRecent().args[0].name).toBe("Race 3");
                expect(Race.save.calls.mostRecent().args[0].raceOrder).toBe(3);

                expect(Race.remove).not.toHaveBeenCalled();

                expect(ctrl.races.length).toBe(3);
            });

            it('should fail add race', function() {
                var race = {};
                race.name = 'Race 3';
                race.order = 3;

                ctrl.addRaceForm = {
                    $setPristine: function() {},
                    $setUntouched: function() {}
                }

                expect(ctrl.races.length).toBe(0);
                deferredQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
                scope.$apply();

                expect(ctrl.races.length).toBe(2);
                expect(ctrl.alert).toBeUndefined();

                ctrl.addRace(race);
                deferredSave.reject();
                scope.$apply();
                expect(ctrl.races.length).toBe(2);

                expect(ctrl.alert).not.toBeUndefined();
                expect(ctrl.alert.msg).toBe("FAILED_ADD");
                expect(ctrl.alert.type).toBe("danger");

                expect(Race.remove).not.toHaveBeenCalled();
            });
        });


       describe('removeRace', function(){
            var scope, deferred, Race;

            beforeEach(inject(function($rootScope, $controller, $q, _Race_) {
                    Race = _Race_;
                    deferredSave = $q.defer();
                    deferredQuery = $q.defer();
                    deferredRemove = $q.defer();
                    spyOn(Race, 'query').and.returnValue({$promise: deferredQuery.promise});
                    spyOn(Race, 'save').and.returnValue({ $promise: deferredSave.promise });
                    spyOn(Race, 'remove').and.returnValue({ $promise: deferredRemove.promise });
                    scope = $rootScope.$new();
                    ctrl = $controller('raceController', {$scope: scope, Race: Race});
                    ctrl.races = [];
            }));

            it('should remove a race from races', function() {
                var race = {};
                race.id = 1;
                expect(ctrl.races.length).toBe(0);
                deferredQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
                scope.$apply();
                expect(ctrl.races.length).toBe(2);
                ctrl.removeRace(race);
                deferredRemove.resolve();
                scope.$apply();

                expect(Race.save).not.toHaveBeenCalled();

                expect(Race.remove.calls.mostRecent().args.length).toBe(1);
                expect(Race.remove.calls.mostRecent().args[0].id).toBe(1);

                expect(ctrl.races.length).toBe(1);
            });

            it('should fail add race', function() {
                var race = {};
                race.id = 1;

                expect(ctrl.races.length).toBe(0);
                deferredQuery.resolve([{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}]);
                scope.$apply();

                expect(ctrl.races.length).toBe(2);
                expect(ctrl.alert).toBeUndefined();

                ctrl.removeRace(race);
                deferredRemove.reject();
                scope.$apply();
                expect(ctrl.races.length).toBe(2);

                expect(ctrl.alert).not.toBeUndefined();
                expect(ctrl.alert.msg).toBe("FAILED_REMOVE");
                expect(ctrl.alert.type).toBe("danger");

                expect(Race.save).not.toHaveBeenCalled();
            });
        });
    });
});
