angular.module('PositionController').directive('notExistingPositionName', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$validators.positionNameExists = function(modelValue, viewValue) {
                if (ctrl.$isEmpty(modelValue)) {
                    return true;
                }

                for (var i = 0; i < scope.ctrl.positions.length; i++) {
                    var position = scope.ctrl.positions[i];
                    if (position.name === viewValue) {
                        return false;
                    }
                }
                return true;
            };
        }
    };
}).directive('notExistingPositionOrder', function() {
    return {
        require: 'ngModel',
        link: function(scope, elm, attrs, ctrl) {
            ctrl.$validators.positionOrderExists = function(modelValue, viewValue) {
                if (ctrl.$isEmpty(modelValue)) {
                    return true;
                }

                var intValue = parseInt(viewValue, 10);
                for (var i = 0; i < scope.ctrl.positions.length; i++) {
                    var position = scope.ctrl.positions[i];
                    if (position.order === intValue) {
                        return false;
                    }
                }

                return true;
            };
        }
    };
});