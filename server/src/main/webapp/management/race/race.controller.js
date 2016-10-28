(function() {
    'use strict'; // NOSONAR

    angular.module('RaceController').controller('raceController', RaceController);

    function RaceController($log, $translate, raceService, alertService) {
        var vm = this;

        vm.addRaceForm = {};
        vm.race = {};

        raceService.getRaces().then(function(data) {
            vm.races = data;

        }, function() {
            alertService.setAlert($translate.instant('FAILED_FETCH'));
        });

        vm.closeAlert = function() {
            alertService.clearAlert();
        };

        vm.removeRace = function(entity) {
            raceService.removeRace(entity).then(function() {
            }, function() {
                alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        };

        vm.addRace = function(data) {
            raceService.addRace({'name': data.name, 'raceOrder': data.order}).then(function() {
                vm.race = {};
                vm.addRaceForm.$setPristine();
                vm.addRaceForm.$setUntouched();

            }, function() {
                vm.addRaceForm.$invalid = true;
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });

        };
    };

})();