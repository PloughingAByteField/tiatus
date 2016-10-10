(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventDropOnAssignedController', ['$scope', '$log', 'AssignedEvent', function($scope, $log, AssignedEvent) {

        function addNewRaceEvent(race, raceEventOrder, item, index) {
            var newRaceEvent = {};
            newRaceEvent.race = race;
            newRaceEvent.raceEventOrder = raceEventOrder;
            newRaceEvent.event = item;
            if ($scope.assignedToRace.length === index || $scope.assignedToRace.length === 0) {
                $scope.assigned.push(newRaceEvent);
            } else {
                $scope.assigned.splice($scope.getIndexOfAssigned($scope.assigned, $scope.assignedToRace[index]), 0, newRaceEvent);
            }

            $scope.unassigned.splice($scope.getIndexOfArray($scope.unassigned, item), 1);
            return newRaceEvent;
        };

        $scope.dropOnAssigned = function(dropEvent, index, item) {
            var updates = [];
            var newRaceEvent = {};
            if (index > $scope.assignedToRace.length) {
                $log.warn("Have index greater than that assigned, bad input");
                return item;
            }

            if (typeof item.raceEventOrder !== 'undefined') {
                $log.debug('Have reassignment of ' + item.event.name + ' from ' + item.raceEventOrder + ' to ' + $scope.assignedToRace[index].raceEventOrder);
                var indexOfItem = $scope.getIndexOfAssigned($scope.assignedToRace, item);
                var raceOrderAtIndex = angular.copy($scope.assignedToRace[index].raceEventOrder);
                $log.debug(indexOfItem);
                if (item.raceEventOrder > $scope.assignedToRace[index].raceEventOrder) {
                    // pushing up the order (1 is the top)
                    $log.debug("Pushing up the order");

                    updates = $scope.updateRaces(index, indexOfItem, 1);
                    $scope.assignedToRace[indexOfItem].raceEventOrder = index + 1;
                    updates.push($scope.assignedToRace[indexOfItem]);

                } else if (item.raceEventOrder < $scope.assignedToRace[index].raceEventOrder) {
                    // pushing down the order
                    $log.debug("Pushing down the order");
                    updates = $scope.updateRaces(indexOfItem + 1, index + 1, -1);
                    $scope.assignedToRace[indexOfItem].raceEventOrder = raceOrderAtIndex;
                    updates.push($scope.assignedToRace[indexOfItem]);

                } else {
                    $log.debug("doing nothing");
                }

            } else {
                if ($scope.assignedToRace.length > 0) {
                    if ($scope.assignedToRace.length === index) {
                        $log.debug('Have assignment ' + item.name + ' to the end of the list');
                        var lastRaceEvent = $scope.assignedToRace[$scope.assignedToRace.length - 1];
                        newRaceEvent = addNewRaceEvent(lastRaceEvent.race, lastRaceEvent.raceEventOrder + 1, item, index);

                    } else {
                        $log.debug('Have assignment ' + item.name + ' to ' + $scope.assignedToRace[index].raceEventOrder);
                        var prevRaceEvent = $scope.assignedToRace[index];
                        $log.debug(prevRaceEvent);
                        newRaceEvent = addNewRaceEvent(prevRaceEvent.race, prevRaceEvent.raceEventOrder, item, index);

                        // update all below this
                        updates = $scope.updateRaces(index, $scope.assignedToRace.length, 1);
                    }

                } else {
                    $log.debug('Have assignment ' + item.name + ' to empty list');
                    newRaceEvent = addNewRaceEvent($scope.races[$scope.getIndexOfArrayOfId($scope.races, $scope.currentRace)], 1, item, 0);
                }
            }

            if (updates.length > 0) {
                $log.debug(updates);
                AssignedEvent.update(updates);
            }

            if (typeof newRaceEvent.race !== 'undefined') {
                $log.debug(newRaceEvent);
                AssignedEvent.save(newRaceEvent);
            }

            $scope.raceChanged();
            $scope.$apply();

            return item;
        };

    }]);

})();