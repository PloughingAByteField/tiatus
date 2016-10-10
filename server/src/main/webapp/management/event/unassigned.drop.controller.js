(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventDropOnUnassignedController', function($scope, $log, AssignedEvent, DeleteAssignedEvent, UnassignedEvent, $translate) {

//            UnassignedEvent.query().$promise.then(function (data) {
//                $scope.unassigned = data;
//            }, function(error) {
//                $log.warn("Failed to get unassigned events", error);
//                $scope.alert = {
//                    type: 'danger',
//                    msg: $translate.instant('FAILED_FETCH')
//                };
//            });

        $scope.dropOnUnassigned = function(dropEvent, index, item) {

            if (typeof item.raceEventOrder !== 'undefined') {
                $log.debug('Have unassignment of ' + item.event.name + ' from ' + item.raceEventOrder);
                var indexOfAssigned = $scope.getIndexOfAssigned($scope.assigned, item);
                var indexOfItem = $scope.getIndexOfAssigned($scope.assignedToRace, item);
                var updates = $scope.updateRaces(indexOfItem + 1, $scope.assignedToRace.length, - 1);
                $scope.assigned.splice(indexOfAssigned, 1);
                $scope.unassigned.push(item.event);

                if (updates.length > 0) {
                    $log.debug(updates);
                    AssignedEvent.update(updates);
                }
                DeleteAssignedEvent.remove(item);

                $scope.raceChanged();
                $scope.$apply();

            } else {
                return false;
            }

            return item;
        };

    });
})();