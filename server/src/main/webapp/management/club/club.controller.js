(function() {
    'use strict'; // NOSONAR

    angular.module('ClubController').controller('clubController', ClubController);

    function ClubController(clubService, alertService, $translate, $log) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.club = {};

        clubService.getClubs().then(function(clubs) {
            vm.clubs = clubs;

        }, function() {
            alertService.setAlert($translate.instant('FAILED_FETCH'));
        });

        vm.closeAlert = function() {
            alertService.clearAlert();
        };

        vm.addClub = function(club) {
            clubService.addClub(club).then(function() {
                vm.club = {};
                vm.addClubForm.$setPristine();
                vm.addClubForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to create event", errResponse);
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        };

        vm.removeClub = function(club) {
            clubService.removeClub(club).then(function() {}, function() {
              alertService.setAlert($translate.instant('FAILED_REMOVE'));
            });
        };

        vm.updateClub = function(club) {
            clubService.updateClub(club).then(function() {}, function() {
              alertService.setAlert($translate.instant('FAILED_UPDATE'));
            });
        };

        function isClubNameInUse(club) {
            for (var i=0; i < vm.clubs.length; i++) {
                var existing = vm.clubs[i];
                if (existing.id !== club.id && existing.club === club.club) {
                    return true;
                }
            }
            return false;
        }

        vm.validClubName = function(club) {
            if (typeof club === 'undefined' || typeof club.club === 'undefined') {
                return false;
            }

            if (club.club.length < 3 || club.club.length > 254) {
                return false;
            }

            return !isClubNameInUse(club);
        };
    };

})();