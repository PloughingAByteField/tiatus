(function() {
    'use strict'; // NOSONAR

    angular.module('RaceController').controller('raceController', RaceController);

    function RaceController($log, $translate, raceService) {
        var vm = this;

        vm.addRaceForm = {};
        vm.race = {};

        raceService.getRaces().then(function(data) {
            vm.races = data;

        }, function() {
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
            raceService.removeRace(entity).then(function() {
            }, function() {
                vm.alert = {
                  type: 'danger',
                  msg: $translate.instant('FAILED_REMOVE')
                };
            });
        };

        vm.addRace = function(data) {
            vm.alert = null;
            raceService.addRace({'name': data.name, 'raceOrder': data.order}).then(function() {
                vm.race = {};
                vm.addRaceForm.$setPristine();
                vm.addRaceForm.$setUntouched();

            }, function() {
                vm.addRaceForm.$invalid = true;
                vm.alert = {
                    type: 'danger',
                    msg: $translate.instant('FAILED_ADD')
                };
            });

        };
    };

})();