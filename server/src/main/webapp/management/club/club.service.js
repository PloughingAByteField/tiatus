(function() {
    'use strict'; // NOSONAR

    angular.module('ClubController').service('clubService', ClubService);

    function ClubService($log, Club, $q) {
        var clubs = undefined;

        var service = {
            getClubs: getClubs,
            addClub: addClub,
            removeClub: removeClub,
            updateClub: updateClub
        };

        return service;

        function getClubs() {
            if (!clubs) {
                var deferred = $q.defer();
                Club.query().$promise.then(function (data) {
                    clubs = data;
                    deferred.resolve(clubs);

                }, function(error) {
                    clubs = error;
                    deferred.reject(error);
                    $log.warn("Failed to get clubs", error);
                });
                clubs = deferred.promise;
            }

            return $q.when(clubs);
        }
        
        function addClub(club) {
            var deferred = $q.defer();
            Club.save(club).$promise.then(function(data) {
                club.id = data.id;
                clubs.push(club);
                deferred.resolve();

            }, function(error) {
                $log.warn("Failed to add club", error);
                deferred.reject(error);

            });
            return $q.when(deferred.promise);
        };

        function removeClub(club) {
            var deferred = $q.defer();
            Club.remove({id: club.id}).$promise.then(function() {
                // remove from list
                clubs.splice(clubs.indexOf(club), 1);
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to remove club", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };

        function updateClub(club) {
            var deferred = $q.defer();
            Club.update(club).$promise.then(function() {
                 for (var i = 0; i < clubs.length; i++) {
                    var c = clubs[i];
                    if (c.id === club.id) {
                        clubs[i] = club;
                        break;
                    }
                }
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to update club", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };
        
    }
})();