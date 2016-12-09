(function() {
    'use strict'; // NOSONAR

    angular.module('EntryController').directive('notExistingEntryNumber', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$validators.entryNumberExists = function(modelValue, viewValue) {
                    if (ctrl.$isEmpty(modelValue)) {
                        return true;
                    }

                    var intValue = parseInt(viewValue, 10);
                    for (var i = 0; i < scope.ctrl.entries.length; i++) {
                        var entry = scope.ctrl.entries[i];
                        if (typeof scope.ctrl.entry !== 'undefined') {
                            if (scope.ctrl.entry.id !== entry.id) {
                                if (entry.number === intValue && entry.race.id === scope.ctrl.currentRace.id) {
                                    return false;
                                }
                            }
                        }
                    }

                    return true;
                };
            }
        };
    });
})();