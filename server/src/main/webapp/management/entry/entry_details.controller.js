(function() {
    'use strict'; // NOSONAR

    angular.module('EntryController').controller('entryDetailsController', EntryDetailsController);

    function EntryDetailsController($log, $location, raceService, alertService, entryService, eventService, eventAssignedService, clubService, $translate) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.entry = entryService.getActiveEntry();
        vm.addEntryForm = {};

        vm.closeAlert = function() {
            alertService.clearAlert();
        };

        function getEntries() {
            entryService.getEntries().then(function(data) {
                vm.entries = data;
            });
        }
        getEntries();

        eventAssignedService.addEventChangeListener(function() {
            getAssignedToRaceEntries(vm.currentRace);
        });

        function getAssignedToRaceEntries(race) {
            vm.eventsAssignedToRace = eventAssignedService.getAssignedEventsForRace(race);
        }

        vm.raceChanged = function(race) {
            raceService.setCurrentRace(race);
            getAssignedToRaceEntries(race);
        };

        raceService.getRaces().then(function(data) {
            vm.races = data;
            raceService.setCurrentRace(vm.races[0]);
            vm.currentRace = raceService.getCurrentRace();
            vm.raceChanged(vm.currentRace);
        });

        clubService.getClubs().then(function(data) {
            vm.clubs = data;
            vm.entry.club = vm.clubs[0];
        });

        eventService.getEvents().then(function(data) {
            vm.events = data;
            vm.entry.event = vm.events[0];
        });

        vm.back = function() {
            entryService.setActiveEntry({});
            $location.path('/entry');
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
            var newEntry = {};
            var timeOnlyEvent = isTimeOnlyEvent(entry);
            newEntry.timeOnly = timeOnlyEvent;
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
            entryService.addEntry(newEntry).then(function() {
                vm.entry = {fixedNumber: false, timeOnly: false};
                vm.addEntryForm.$setPristine();
                vm.addEntryForm.$setUntouched();
                $location.path('/entry');

            }, function() {
                $log.warn("Failed to save entry");
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        };

        vm.updateEntry = function(entry) {
            entryService.updateEntry(entry).then(function() {
                vm.entry = {fixedNumber: false, timeOnly: false};
                vm.addEntryForm.$setPristine();
                vm.addEntryForm.$setUntouched();
                $location.path('/entry');

            }, function() {
                $log.warn("Failed to update entry");
                alertService.setAlert($translate.instant('FAILED_UPDATE'));
            });

        };
    }

})();