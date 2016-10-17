(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').service('eventAssignedService', EventAssignedService);

    // this an intermediary caching service shared across the controllers
    function EventAssignedService($log, AssignedEvent, $q) {
        var assigned = undefined;

        var service = {
            getAssigned: getAssigned,
            addAssigned: addAssigned
        };

        return service;

        function getAssigned() {
            if (!assigned) {
                var deferred = $q.defer();
                AssignedEvent.query().$promise.then(function (data) {
                    assigned = data;
                    deferred.resolve(assigned);

                }, function(error) {
                    assigned = error;
                    deferred.reject(error);
                    $log.warn("Failed to get assigned events", error);
                });
                assigned = deferred.promise;
            }

            return $q.when(assigned);
        }

        function addAssigned(event) {
            assigned.push(event);
            console.log(assigned);
        };

    };

})();