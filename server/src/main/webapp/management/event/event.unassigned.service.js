(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').service('eventUnassignedService', EventUnassignedService);

    function EventUnassignedService($log, UnassignedEvent, Event, $q) {
        var unassigned = undefined;

        var service = {
            getUnassigned: getUnassigned,
            unassignEvent: unassignEvent,
            assignEvent: assignEvent,
            createUnassignedEvent: createUnassignedEvent,
            removeUnassigned: removeUnassigned
        };

        return service;

        function getUnassigned() {
            if (!unassigned) {
                var deferred = $q.defer();
                UnassignedEvent.query().$promise.then(function (data) {
                    unassigned = data;
                    deferred.resolve(unassigned);

                }, function(error) {
                    unassigned = error;
                    deferred.reject(error);
                    $log.warn("Failed to get unassigned events", error);
                });
                unassigned = deferred.promise;
            }

            return $q.when(unassigned);
        }

        function createUnassignedEvent(event) {
            var deferred = $q.defer();
            Event.save(event).$promise.then(function(data) {
                event.id = data.id;
                unassigned.push(event);
                deferred.resolve();

            }, function(error) {
                $log.warn("Failed to create event", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };

        function unassignEvent(item, index) {
            unassigned.splice(index, 0, item);
        };

        function assignEvent(item) {
            deleteUnassigned(item);
        };

        function deleteUnassigned(item) {
            var position = getPosition(item);
            if (position !== -1) {
                unassigned.splice(position, 1);
            }
        };

        function getPosition(item) {
            for (var i = 0; i < unassigned.length; i++) {
                var element = unassigned[i];
                if (element.id === item.id) {
                    return i;
                }
            }
            return -1;
        };

        function removeUnassigned(eventId) {
            var deferred = $q.defer();
            Event.remove(eventId).$promise.then(function() {
                // remove from list
                deleteUnassigned(item);
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to remove event", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };
    };


})();