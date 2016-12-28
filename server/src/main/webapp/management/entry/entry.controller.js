(function() {
    'use strict'; // NOSONAR

    angular.module('EntryController').controller('entryController', EntryController);

    function EntryController($log, $location, $filter, raceService, alertService, entryService, eventService, eventAssignedService, clubService, NgTableParams, $translate) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.entry = entryService.getActiveEntry();

        vm.closeAlert = function() {
            alertService.clearAlert();
        };

        eventAssignedService.addEventChangeListener(function() {
            getAssignedToRaceEntries(vm.currentRace);
        });

        entryService.addEntryChangeListener(function() {
            getEntries();
        });

        vm.raceChanged = function(race) {
            raceService.setCurrentRace(race);
            getAssignedToRaceEntries(race);
        };

        raceService.getRaces().then(function(data) {
            vm.races = data;
            raceService.setCurrentRace(vm.races[0]);
            vm.currentRace = raceService.getCurrentRace();
            dataReady();
        });

        function getEntries() {
            entryService.getEntries().then(function(data) {
                vm.entries = data;
                // expand out the clubs and events from the data
                for (var i=0; i < vm.entries.length; i++) {
                    var entry = vm.entries[i];
                    entry.club = concatClubs(entry.clubs);
                }
                dataReady();
            });
        }
        getEntries();

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

        eventAssignedService.getAssignedEvents().then(function(data) {
            vm.assigned = data;
            dataReady();
        });

        clubService.getClubs().then(function(data) {
            vm.clubs = data;
            vm.entry.club = vm.clubs[0];
        });

        eventService.getEvents().then(function(data) {
            vm.events = data;
            vm.entry.event = vm.events[0];
        });

        vm.newEntry = function() {
            entryService.setActiveEntry({fixedNumber: false, timeOnly: false});
            $location.path('/entry_details');
        };

        function getAssignedToRaceEntries(race) {
            vm.eventsAssignedToRace = eventAssignedService.getAssignedEventsForRace(race);
            vm.entriesForRace = $filter('filter')(vm.entries, function (entry) {
                return entry.race.id == race.id;
            });
            vm.tableParams = new NgTableParams({count: 10}, {counts: [], dataset: vm.entriesForRace});
        }

        function dataReady() {
            if (typeof vm.entries !== 'undefined' && typeof vm.currentRace !== 'undefined' && typeof vm.assigned !== 'undefined') {
                vm.raceChanged(vm.currentRace);
            }
        }

        vm.removeEntry = function(entry) {
            entryService.removeEntry(entry).then(function() {

            }, function() {
                $log.warn("Failed to remove entry");
                alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        };

        vm.updateEntry = function(entry) {
            entryService.setActiveEntry(entry);
            $location.path('/entry_details');
        };
    }

})();