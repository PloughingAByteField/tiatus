(function() {
    'use strict'; // NOSONAR

    angular.module('ClubController').directive('notExistingClubName', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$validators.clubNameExists = function(modelValue, viewValue) {
                    if (ctrl.$isEmpty(modelValue)) {
                        return true;
                    }

                    for (var i = 0; i < scope.ctrl.clubs.length; i++) {
                        var club = scope.ctrl.clubs[i];
                        if (club.club === viewValue) {
                            return false;
                        }
                    }
                    return true;
                };
            }
        };
    });
})();