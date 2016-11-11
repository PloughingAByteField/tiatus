(function() {
    'use strict'; // NOSONAR

    angular.module('PositionController').service('positionService', PositionService);

    // this an intermediary caching service shared across the controllers
    function PositionService($log, Position, $q) {
        var positions = undefined;

        var service = {
            getPositions: getPositions,
            addPosition: addPosition,
            removePosition: removePosition,
            updatePosition: updatePosition
        };

        function getPositions() {
            if (!positions) {
                var deferred = $q.defer();
                Position.query().$promise.then(function (data) {
                    positions = data;
                    deferred.resolve(positions);

                }, function(error) {
                    deferred.reject(error);
                    $log.warn("Failed to get positions", error);
                });
                positions = deferred.promise;
            }

            return $q.when(positions);
        }

        function addPosition(position) {
            var deferred = $q.defer();
            Position.save(position).$promise.then(function(data) {
                position.id = data.id;
                // add to list
                positions.push(position);
                deferred.resolve();

            }, function(error) {
                $log.warn("Failed to add position", error);
                deferred.reject(error);

            });
            return $q.when(deferred.promise);
        }

        function removePosition(position) {
            var deferred = $q.defer();
            Position.remove(position).$promise.then(function() {
                // remove from list
                positions.splice(positions.indexOf(position), 1);
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to remove position", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        }

        function updatePosition(position) {
            var deferred = $q.defer();
            Position.update(position).$promise.then(function() {
                for (var i = 0; i < positions.length; i++) {
                    var p = positions[i];
                    if (p.id === position.id) {
                        positions[i] = position;
                        break;
                    }
                }
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to update position", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        }

        return service;
    };

})();