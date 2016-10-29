(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventDropOnUnassignedController', EventUnassignedDropController);

    function EventUnassignedDropController($log, eventUnassignedService, eventAssignedService, raceService, alertService, $translate) {
        var vm = this;
        eventUnassignedService.getUnassigned().then(function(data) {
            vm.unassigned = data;
        });

        vm.dropOnUnassigned = function(dropEvent, index, item) {
            if (typeof item.raceEventOrder !== 'undefined') {
                $log.debug('Have unassignment of ' + item.event.name + ' from ' + item.raceEventOrder);
                eventUnassignedService.unassignEvent(item.event, index);
                eventAssignedService.unassignEvent(item, raceService.getCurrentRace()).then(function() {}, function() {
                    dropFailure();
                });

            } else {
                return false;
            }

            return true;
        };

        function dropFailure() {
            $log.warn("Failed to drop item on unassigned");
            alertService.setAlert($translate.instant('FAILED_UNASSIGN'));
        };
    };
})();