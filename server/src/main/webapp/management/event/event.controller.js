(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').controller('eventController', EventController);

    function EventController($filter, $log, Race, Event, AssignedEvent, $translate) {
        var vm = this;
        vm.assigned = [];
        vm.unassigned = [];

        Race.query().$promise.then(function(data) {
            vm.races = data;
            if (data.length > 0) {
                vm.currentRace = vm.races[0].id;
            }
        }, function(error) {
            $log.warn("Failed to get races", error);
            vm.alert = {
                type: 'danger',
                msg: $translate.instant('FAILED_FETCH')
            };
        });

//        AssignedEvent.query().$promise.then(function (data) {
//            setAssignedToRace(data);
//            vm.assigned = data;
//        }, function(error) {
//            $log.warn("Failed to get assigned events", error);
//            vm.alert = {
//                type: 'danger',
//                msg: $translate.instant('FAILED_FETCH')
//            };
//        });

        vm.closeAlert = function() {
            vm.alert = null;
        };

        function setAssignedToRace(data) {
            var result = [];
            for (var i = 0; i < data.length; i++) {
                var raceEvent = data[i];
                if (raceEvent.race !== null && raceEvent.race.id === vm.currentRace) {
                    result.push(raceEvent);
                }
            }
            vm.assignedToRace = $filter('orderBy')(result, "raceEventOrder");
        };

        function getArrayIndex(array, element, callback) {
            var index = -1;
            for (var i = 0; i < array.length; i++) {
                if (callback(array[i], element)) {
                    index = i;
                    break;
                }
            }
            return index;
        };

        vm.getIndexOfAssigned = function(array, item) {
            return getArrayIndex(array, item, function(raceEvent, item) { return raceEvent.race.id === vm.currentRace && raceEvent.event.id === item.event.id; });
        };

        vm.getIndexOfArrayOfId = function(array, id) {
            return getArrayIndex(array, id, function(item, id) { return item.id === id; });
        };

        vm.getIndexOfArray = function(array, item) {
            return vm.getIndexOfArrayOfId(array, item.id);
        };

        vm.raceChanged = function() {
            setAssignedToRace(vm.assigned);
        };

        vm.updateRaces = function(start, stop, value) {
            var updates = [];
            for (var i = start; i < stop; i++) {
                vm.assignedToRace[i].raceEventOrder = vm.assignedToRace[i].raceEventOrder + value;
                updates.push(vm.assignedToRace[i]);
            }
            return updates;
        };

        vm.deleteEvent = function(id) {
            vm.alert = null;
            $log.debug(id);
            var event = new Event();
            event.id = id;
            Event.remove(event).$promise.then(function() {
                $log.debug(vm.getIndexOfArrayOfId(vm.unassigned, event.id));
//                vm.unassigned.splice(vm.getIndexOfArrayOfId(vm.unassigned, event.id), 1);

            }, function(errResponse) {
                $log.warn("Failed to delete event", errResponse);
                vm.alert = {
                    type: 'danger',
                    msg: $translate.instant('FAILED_REMOVE')
                };
            });
        };

        vm.createEvent = function(e) {
            vm.alert = null;
            $log.debug(e);
            var event = new Event();
            event.name = e.name;
            event.weighted = e.weighted;
            $log.debug(event);
            Event.save(event).$promise.then(function(data) {
                event.id = data.id;
//                vm.unassigned.push(event);
                vm.event = {};
                vm.addEventForm.$setPristine();
                vm.addEventForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to add event", errResponse);
                vm.alert = {
                    type: 'danger',
                    msg: $translate.instant('FAILED_ADD')
                };
            });
        };
    };

})();