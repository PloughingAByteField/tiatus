(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventDropOnAssignedController', EventAssignedDropController);

    function EventAssignedDropController($log, eventAssignedService, eventUnassignedService, raceService, $translate, alertService) {
        var vm = this;
        vm.assigned = eventAssignedService.getAssignedEvents().then(function(data) {
            vm.assigned = data;
        });

        eventUnassignedService.getUnassigned().then(function(data) {
            vm.unassigned = data;
        });

        vm.assignedToRace = function(raceId) {
            return eventAssignedService.getAssignedEventsForRace(raceId);
        };

//        function addNewRaceEvent(race, raceEventOrder, item, index) {
//            var newRaceEvent = {};
//            newRaceEvent.race = race;
//            newRaceEvent.raceEventOrder = raceEventOrder;
//            newRaceEvent.event = item;
//            if ($scope.assignedToRace.length === index || $scope.assignedToRace.length === 0) {
//                $scope.assigned.push(newRaceEvent);
//            } else {
//                $scope.assigned.splice($scope.getIndexOfAssigned($scope.assigned, $scope.assignedToRace[index]), 0, newRaceEvent);
//            }
//
//            $scope.unassigned.splice($scope.getIndexOfArray($scope.unassigned, item), 1);
//            return newRaceEvent;
//        };

        vm.dropOnAssigned = function(dropEvent, index, item) {
            var updates = [];
            var newRaceEvent = {};

            // are we a self drop
            if (typeof item.raceEventOrder !== 'undefined') {
                eventAssignedService.reassignEvent(item, raceService.getCurrentRace(), index).then(function(data) {}, function(error) {
                   dropFailure();
                });
            } else {
                eventAssignedService.assignEvent(item, raceService.getCurrentRace(), index).then(function(data) {
                   eventUnassignedService.assignEvent(item);
                }, function(error) {
                   dropFailure();
                });
            }
            return true;
        };

        function dropFailure() {
            $log.warn("Failed to drop item on assigned");
            alertService.setAlert($translate.instant('FAILED_ASSIGN'));
        };

    };

})();