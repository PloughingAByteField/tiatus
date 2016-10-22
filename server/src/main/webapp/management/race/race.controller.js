(function() {
    'use strict'; // NOSONAR

    angular.module('RaceController').controller('raceController', RaceController);

    function RaceController($log, Race, $translate, raceService) {
        var vm = this;

        vm.addRaceForm = {};
        vm.race = {};

        raceService.getRaces().then(function(data) {
            vm.races = data;

        }, function(error) {
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
            raceService.removeRace(entity).then(function(data) {
            }, function(error) {
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
            raceService.addRace(newRace).then(function(data) {
                vm.race = {};
                vm.addRaceForm.$setPristine();
                vm.addRaceForm.$setUntouched();

            }, function(error) {
                vm.addRaceForm.$invalid = true;
                vm.alert = {
                    type: 'danger',
                    msg: $translate.instant('FAILED_ADD')
                };
            });

        };
    };

})();