(function() {
    'use strict'; // NOSONAR

    angular.module('PositionController').controller('positionController', PositionController);

    function PositionController($log, positionService, alertService, $translate) {
        var vm = this;
        vm.alert = alertService.getAlert();
        vm.position = {timing: false, showAllEntries: false, active: false, canStart: false};

        positionService.getPositions().then(function(data) {
            vm.positions = data;
        });

        vm.addPosition = function(position) {
            positionService.addPosition(position).then(function() {
                vm.position = {timing: false, showAllEntries: false, active: false, canStart: false};
                vm.addPositionForm.$setPristine();
                vm.addPositionForm.$setUntouched();

            }, function(errResponse) {
                $log.warn("Failed to create position", errResponse);
                alertService.setAlert($translate.instant('FAILED_ADD'));
            });
        };

        vm.updatePosition = function(position) {
            positionService.updatePosition(position).then(function() {

            }, function(errResponse) {
                $log.warn("Failed to update position", errResponse);
                alertService.setAlert($translate.instant('FAILED_UPDATE'));
            });
        };

        vm.deletePosition = function(position) {
            positionService.removePosition(position).then(function() {

            }, function(errResponse) {
                $log.warn("Failed to delete position", errResponse);
                alertService.setAlert($translate.instant('FAILED_DELETE'));
            });
        };

        vm.closeAlert = function() {
            alertService.clearAlert();
        };
    };

})();