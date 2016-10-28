angular.module('RaceController').directive('notExistingRaceName', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$validators.raceNameExists = function(modelValue, viewValue) {
                if (ctrl.$isEmpty(modelValue)) {
                    return true;
                }

                for (var i = 0; i < scope.ctrl.races.length; i++) {
                    var race = scope.ctrl.races[i];
                    if (race.name === viewValue) {
                        return false;
                    }
                }
                return true;
            };
        }
    };
}).directive('notExistingRaceNumber', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$validators.raceNumberExists = function(modelValue, viewValue) {
                if (ctrl.$isEmpty(modelValue)) {
                    return true;
                }

                var intValue = parseInt(viewValue, 10);
                for (var i = 0; i < scope.ctrl.races.length; i++) {
                    var race = scope.ctrl.races[i];
                    if (race.raceOrder === intValue) {
                        return false;
                    }
                }

                return true;
            };
        }
    };
});