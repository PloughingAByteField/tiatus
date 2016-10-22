(function() {
    'use strict'; // NOSONAR

    angular.module('EventController').directive('notExistingEventName', function() {
        return {
            require: 'ngModel',
            link: function(scope, elm, attrs, ctrl) {
                ctrl.$validators.eventNameExists = function(modelValue, viewValue) {
                    if (ctrl.$isEmpty(modelValue)) {
                        return true;
                    }

                    for (var i = 0; i < scope.ctrl.assigned.length; i++) {
                        var eventsForRace = scope.ctrl.assigned[i];
                        for (var x = 0; x < eventsForRace.events.length; x++) {
                            var assignedEvent = eventsForRace.events[x].event;
                            if (assignedEvent.name === viewValue) {
                                return false;
                            }
                        }
                    }

                    for (var x = 0; x < scope.ctrl.unassigned.length; x++) {
                        var unassignedEvent = scope.ctrl.unassigned[x];
                        if (unassignedEvent.name === viewValue) {
                            return false;
                        }
                    }

                    return true;
                };
            }
        };
    });
})();