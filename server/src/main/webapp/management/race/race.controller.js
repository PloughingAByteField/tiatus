(function() {
    'use strict'; // NOSONAR

    angular.module('RaceController', ['ngTable', 'ui.bootstrap']).controller('raceController', RaceController);

    function RaceController($filter, $log, $timeout, Race, $translate) {
        var vm = this;

        vm.addRaceForm = {};
        vm.race = {};

        Race.query().$promise.then(function(data) {
            vm.races = data;

        }, function(errResponse) {
            $log.warn("Failed to get races", errResponse);
            vm.alert = {
                type: 'danger',
                msg: $translate.instant('FAILED_FETCH')
            };
        });

        vm.closeAlert = function() {
            vm.alert = null;
        };

        vm.removeRace = function(entity) {
            vm.alert = null;
            var race = new Race();
            race.id = entity.id;
            Race.remove(race).$promise.then(function() {
                // remove from list
                vm.races.splice(vm.races.indexOf(entity), 1);

            }, function(errResponse) {
                $log.warn("Failed to remove race", errResponse);
                vm.alert = {
                    type: 'danger',
                    msg: $translate.instant('FAILED_REMOVE')
                };
            });

        };

        vm.addRace = function(data) {
            vm.alert = null;
            var newRace = new Race();
            newRace.name = data.name;
            newRace.raceOrder = data.order;
            Race.save(newRace).$promise.then(function(response) {
                // add to list
                vm.races.push(response);
                vm.race = {};
                vm.addRaceForm.$setPristine();
                vm.addRaceForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to add race", errResponse);
                vm.addRaceForm.$invalid = true;
                vm.alert = {
                    type: 'danger',
                    msg: $translate.instant('FAILED_ADD')
                };
            });
        };
    };

})();