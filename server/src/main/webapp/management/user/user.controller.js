(function() {
    'use strict'; // NOSONAR

    angular.module('UserController').controller('userController', UserController);

    function UserController($log, $translate, userService, alertService) {
        var vm = this;
        vm.user = {};
        vm.userRoles = [];

        userService.getUsers().then(function(data) {
            vm.users = data;
            $log.debug(vm.users);

        }, function() {
            alertService.setAlert($translate.instant('FAILED_FETCH'));
        });

        userService.getUserRoles().then(function(data) {
            vm.userRoles = data;
            $log.debug(vm.userRoles);
            vm.timingRole = vm.userRoles.filter(function(role){ return role.roleName === 'TIMING'}).shift();
            console.log(vm.timingRole);
            vm.user.type = vm.timingRole;

        }, function() {
            alertService.setAlert($translate.instant('FAILED_FETCH'));
        });

        vm.addUser = function(data) {
            console.log(data);
            var user = {'userName': data.name, 'password': data.password, 'roles': [{'role': data.type}]};
            userService.addUser(user).then(function(response) {
                vm.user = {};
                vm.user.type = vm.timingRole;
                vm.addUserForm.$setPristine();
                vm.addUserForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to create user", errResponse);
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        }

        vm.removeUser = function(user) {
            if (vm.getRole(user) === 'MANAGEMENT') {
                console.log('admin user');
                // are we the sole admin user -- return
                var adminUsers = vm.users.filter(function(u){ return vm.getRole(u) === 'MANAGEMENT'});
                console.log(adminUsers);
                if (adminUsers.length === 1) {
                    $log.warn('Only have one admin user, will not delete');
                    return;
                }
            }

            userService.removeUser(user).then(function() {},
                function() {
                 alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        }

        vm.updateUser = function(user) {
            userService.updateUser(user).then(function() {},
                function() {
                 alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        }

        vm.getRole = function(user) {
            if (user.roles.length === 0) {
                return '';
            }
            if (user.roles[0].role.roleName === "ADMIN"){
                return "MANAGEMENT";
            } else if (user.roles[0].role.roleName === "ADJUDICATOR"){
                return "ADJUDICATOR";
            } else {
                return "TIMING";
            }

        }
    };

})();