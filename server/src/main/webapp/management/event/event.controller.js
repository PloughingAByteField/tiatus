(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventController', EventController);

    function EventController($log, Event, $translate, eventAssignedService, eventUnassignedService, raceService, alertService) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.event = { weighted: false };

        eventAssignedService.getAssignedEvents().then(function(data) {
            vm.assigned = data;
            raceDataChange();
        });

        eventUnassignedService.getUnassigned().then(function(data) {
            vm.unassigned = data;
        });

        raceService.getRaces().then(function(data) {
            vm.races = data;
            raceService.setCurrentRace(vm.races[0].id);
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

        vm.raceChanged = function(raceId) {
            vm.assignedToRace = eventAssignedService.getAssignedEventsForRace(raceId);
            raceService.setCurrentRace(raceId);
        };

        vm.createEvent = function(e) {
            var event = new Event();
            event.name = e.name;
            event.weighted = e.weighted;
            eventUnassignedService.createUnassignedEvent(event).then(function(data) {
                vm.event = { weighted: false };
                vm.addEventForm.$setPristine();
                vm.addEventForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to create event", errResponse);
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        };

        vm.deleteEvent = function(id) {
            eventUnassignedService.removeUnassigned({'id': id}).then(function(data) {}, function(error) {
              alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        };
    };

})();