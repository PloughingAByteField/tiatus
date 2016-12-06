(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').service('eventService', EventService);

    // this an intermediary caching service shared across the controllers
    function EventService($log, Event, $q, $filter) {
        var events = undefined;
        var eventChangeListeners = [];

        var service = {
            getEvents: getEvents
        };

        return service;

        function getEvents() {
             if (!events) {
                var deferred = $q.defer();
                Event.query().$promise.then(function (data) {
                    events = data;
                    deferred.resolve(events);

                }, function(error) {
                    events = error;
                    deferred.reject(error);
                    $log.warn("Failed to get events", error);
                });
                events = deferred.promise;
            }

            return $q.when(events);
        }

        function addEventChangeListener(listener) {
            eventChangeListeners.push(listener);
        }

        function notifyEventChangeListener() {
            for (var i = 0; i < eventChangeListeners.length; i++) {
                eventChangeListeners[i]();
            }
        }
    };

})();