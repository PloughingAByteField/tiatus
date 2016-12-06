(function() {
    'use strict'; // NOSONAR

    angular.module('EntryController').controller('entryController', EntryController);

    function EntryController($log, raceService, alertService, entryService, eventService, eventAssignedService, clubService, NgTableParams) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.entry = {fixedNumber: false, timeOnly: false};

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
        console.log('race change');
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

        function getAssignedToRaceEntries(race) {
            vm.eventsAssignedToRace = eventAssignedService.getAssignedEventsForRace(race);
            vm.tableParams = new NgTableParams({count: 10}, {counts: [], dataset: vm.entries});
        };

        function dataReady() {
            if (typeof vm.entries !== 'undefined' && typeof vm.currentRace !== 'undefined' && typeof vm.assigned !== 'undefined') {
                vm.raceChanged(vm.currentRace);
            }
        };

        function isTimeOnlyEvent(entry) {
            // is this a timed only event by not being in eventsAssignedToRace
            for (var i=0; i < vm.eventsAssignedToRace.length; i++) {
                var assignedEvent = vm.eventsAssignedToRace[i];
                if (assignedEvent.event.id === entry.event.id) {
                    return false;
                }
            }
            return true;
        }

        function fillNewEntry(entry) {
            var timeOnlyEvent = isTimeOnlyEvent(entry);
            var newEntry = {};
            if (entry.fixedNumber && typeof entry.number !== 'undefined') {
                newEntry.number = entry.number;
                newEntry.fixedNumber = true;
            }

            if (typeof entry.crew !== 'undefined') {
                newEntry.crew = entry.crew;
            }

            if (typeof entry.weighting !== 'undefined') {
                newEntry.weighting = entry.weighting;
            }

            newEntry.event = entry.event;
            newEntry.clubs = [];
            for (var x=0; x < entry.clubs.length; x++) {
                newEntry.clubs.push(entry.clubs[x]);
            }
            newEntry.race = {id: vm.currentRace.id};

            return newEntry;
        }

        vm.addEntry = function(entry) {
            var newEntry = fillNewEntry(entry);
            console.log(newEntry);
            entryService.addEntry(newEntry).then(function() {
                vm.entry = {fixedNumber: false, timeOnly: false};
                vm.addEntryForm.$setPristine();
                vm.addEntryForm.$setUntouched();
            }, function() {
                $log.warn("Failed to save entry");
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        };

        vm.removeEntry = function(entry) {
            entryService.removeEntry(entry).then(function() {

            }, function() {
                $log.warn("Failed to save entry");
                alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        };

        vm.updateEntry = function(entry, form) {
            console.log(form);
//            entryService.removeEntry(entry).then(function() {
//
//            }, function() {
//                $log.warn("Failed to save entry");
//                alertService.setAlert($translate.instant('FAILED_REMOVE'));
//            });
        };
    };

})();