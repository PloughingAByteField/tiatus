(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventController', EventController);

    function EventController($log, $translate, eventAssignedService, eventUnassignedService, raceService, positionService, alertService) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.event = { weighted: false };

        positionService.getPositions().then(function(data) {
            vm.positions = data;
        });

        eventAssignedService.addEventChangeListener(function() {
            updateAssignedToRaceEvents(vm.currentRace);
        });

        eventAssignedService.getAssignedEvents().then(function(data) {
            vm.assigned = data;
            raceDataChange();
        });

        eventUnassignedService.getUnassigned().then(function(data) {
            vm.unassigned = data;
        });

        raceService.getRaces().then(function(data) {
            vm.races = data;
            raceService.setCurrentRace(vm.races[0]);
            vm.currentRace = raceService.getCurrentRace();
            raceDataChange();
        });

        function raceDataChange() {
            if (vm.assigned && vm.currentRace) {
                vm.raceChanged(vm.currentRace);
            }
        };

        vm.closeAlert = function() {
            alertService.clearAlert();
        };

        vm.raceChanged = function(race) {
            raceService.setCurrentRace(race);
            updateAssignedToRaceEvents(race);
        };

        function updateAssignedToRaceEvents(race) {
            vm.assignedToRace = eventAssignedService.getAssignedEventsForRace(race);
        };

        vm.createEvent = function(e) {
            eventUnassignedService.createUnassignedEvent({'name': e.name, 'weighted': e.weighted}).then(function() {
                vm.event = { weighted: false,  };
                vm.addEventForm.$setPristine();
                vm.addEventForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to create event", errResponse);
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        };

        vm.deleteEvent = function(id) {
            eventUnassignedService.removeUnassigned({'id': id}).then(function() {}, function() {
              alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        };
    };

})();