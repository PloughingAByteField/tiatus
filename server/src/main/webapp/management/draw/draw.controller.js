(function() {
    'use strict'; // NOSONAR

    angular.module('DrawController').controller('drawController', DrawController);

    function DrawController($log, $filter, alertService, raceService, drawService, $translate) {
        var vm = this;
        vm.alert = alertService.getAlert();

        drawService.addEntryChangeListener(function(){
            getEntries();
        });

        vm.dropEntry= function(dropEvent, index, item) {
            drawService.setEntryPosition(item, index);

        };

        vm.raceChanged = function(race) {
            raceService.setCurrentRace(race);
            if (typeof vm.entries !== 'undefined') {
                vm.entriesForRace = $filter('filter')(vm.entries, function (entry) {
                    return entry.race.id == raceService.getCurrentRace().id;
                });
            }
        };

        raceService.getRaces().then(function(data) {
            vm.races = data;
            raceService.setCurrentRace(vm.races[0]);
            vm.currentRace = raceService.getCurrentRace();
        });


        getEntries();

        function getEntries() {
            drawService.getEntries().then(function(data) {
                vm.entries = data;
                // expand out the clubs and events from the data
                for (var i=0; i < vm.entries.length; i++) {
                    var entry = vm.entries[i];
                    entry.club = concatClubs(entry.clubs);
                }
                if (typeof raceService.getCurrentRace() !== 'undefined') {
                    vm.entriesForRace = $filter('filter')(vm.entries, function (entry) {
                        return entry.race.id == raceService.getCurrentRace().id;
                    });
                }
            });
        }

        function concatClubs(clubsList) {
            var clubs = "";
            for (var x=0; x < clubsList.length; x++) {
                if (x > 0) {
                    clubs = clubs.concat(" / ");
                }
                clubs = clubs.concat(clubsList[x].clubName);
            }

            return clubs;
        }


    }

})();