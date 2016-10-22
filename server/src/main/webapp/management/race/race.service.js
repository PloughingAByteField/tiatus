(function() {
    'use strict'; // NOSONAR

    angular.module('RaceController').service('raceService', RaceService);

    // this an intermediary caching service shared across the controllers
    function RaceService($log, Race, $q) {
        var races = undefined;
        var currentRace = {};

        var service = {
            getRaces: getRaces,
            addRace: addRace,
            removeRace: removeRace,
            getCurrentRace: getCurrentRace,
            setCurrentRace: setCurrentRace
        };

        return service;

        function getRaces() {
            if (!races) {
                var deferred = $q.defer();
                Race.query().$promise.then(function (data) {
                    races = data;
                    deferred.resolve(races);

                }, function(error) {
                    races = error;
                    deferred.reject(error);
                    $log.warn("Failed to get races", error);
                });
                races = deferred.promise;
            }

            return $q.when(races);
        }

        function addRace(race) {
            var deferred = $q.defer();
            Race.save(race).$promise.then(function(response) {
                // add to list
                races.push(response);
                deferred.resolve(response);

            }, function(error) {
                $log.warn("Failed to add race", error);
                deferred.reject(error);

            });
            return $q.when(deferred.promise);
        };

        function removeRace(race) {
            var deferred = $q.defer();
            Race.remove(race).$promise.then(function() {
                // remove from list
                races.splice(races.indexOf(race), 1);
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to remove race", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };

        function getCurrentRace() {
            return currentRace;
        }

        function setCurrentRace(race) {
            currentRace = race;
        }
    };

})();