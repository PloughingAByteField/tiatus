(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').service('eventUnassignedService', EventUnassignedService);

    function EventUnassignedService($log, UnassignedEvent, $q) {
        var unassigned = undefined;

        var service = {
            getUnassigned: getUnassigned,
            addUnassigned: addUnassigned
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

        function addUnassigned(event) {
            unassigned.push(event);
            console.log(unassigned);
        };
    };


})();