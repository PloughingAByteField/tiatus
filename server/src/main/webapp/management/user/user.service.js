(function() {
    'use strict'; // NOSONAR

    angular.module('UserController').service('userService', UserService);

    // this an intermediary caching service shared across the controllers
    function UserService($log, User, $q) {
        var users = undefined;
        var userRoles = undefined;

        var service = {
            getUsers: getUsers,
            getUserRoles: getUserRoles,
            addUser: addUser,
            removeUser: removeUser,
            updateUser: updateUser
        };

        return service;

        function getUsers() {
            if (!users) {
                var deferred = $q.defer();
                User.query().$promise.then(function (data) {
                    users = data;
                    deferred.resolve(users);

                }, function(error) {
                    users = error;
                    deferred.reject(error);
                    $log.warn("Failed to get users", error);
                });
                users = deferred.promise;
            }

            return $q.when(users);
        }

        function getUserRoles() {
            if (!userRoles) {
                var deferred = $q.defer();
                User.roles().$promise.then(function (data) {
                    userRoles = data;
                    deferred.resolve(userRoles);

                }, function(error) {
                    userRoles = error;
                    deferred.reject(error);
                    $log.warn("Failed to get userRoles", error);
                });
                userRoles = deferred.promise;
            }

            return $q.when(userRoles);
        }

        function addUser(user) {
            var deferred = $q.defer();
            User.save(user).$promise.then(function(response) {
                // add to list
                users.push(response);
                deferred.resolve(response);

            }, function(error) {
                $log.warn("Failed to add user", error);
                deferred.reject(error);

            });
            return $q.when(deferred.promise);
        };

        function removeUser(user) {
            var deferred = $q.defer();
            User.remove(user).$promise.then(function() {
                // remove from list
                users.splice(users.indexOf(user), 1);
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to remove user", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };

        function updateUser(user) {
            var deferred = $q.defer();
            User.update(user).$promise.then(function() {
                deferred.resolve();
            }, function(error) {
                $log.warn("Failed to update user", error);
                deferred.reject(error);
            });
            return $q.when(deferred.promise);
        };
    };

})();