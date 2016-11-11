describe("src.test.javascript.management.position.controller.spec.js", function() {

    beforeEach(module(function($logProvider) { // test fail other wise
        // We can configure the debugging level (the default is true)
        $logProvider.debugEnabled(false);
    }));

    beforeEach(module('managementApp', function ($provide, $translateProvider) {

        $provide.factory('customLoader', function ($q) {
            return function () {
                var deferred = $q.defer();
                deferred.resolve({});
                return deferred.promise;
            };
        });

        $translateProvider.useLoader('customLoader');
    }));

    describe('positionController', function(){

        var scope, deferred, positionService, alertService;

        beforeEach(inject(function($rootScope, $controller, $q, _positionService_, _alertService_, $httpBackend) {
            positionService = _positionService_;
            alertService = _alertService_;
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(positionService, 'getPositions').and.returnValue(deferredQuery.promise);
            spyOn(positionService, 'addPosition').and.returnValue(deferredSave.promise);
            spyOn(positionService, 'removePosition').and.returnValue(deferredRemove.promise);
            spyOn(positionService, 'updatePosition').and.returnValue(deferredSave.promise);
            spyOn(alertService, 'clearAlert').and.returnValue();
            scope = $rootScope.$new();
            ctrl = $controller('positionController', {$scope: scope, positionService: positionService, alertService: alertService});
            $httpBackend.whenGET('home/home.html').respond();
        }));

        function getPositions() {
            deferredQuery.resolve([{id: 1, name: 'Position 1', order: 1}, {id: 2, name: 'Position 2', order: 2}]);
            scope.$apply();
        };

        it('should get positions', function() {
            expect(ctrl.positions).toBeUndefined();
            getPositions();
            expect(positionService.getPositions).toHaveBeenCalled();
            expect(ctrl.positions.length).toBe(2);
        });

        it('should call clearAlert', function() {
            ctrl.closeAlert();
            expect(alertService.clearAlert).toHaveBeenCalled();
        });

        it('should call create position', function() {
            getPositions();
            ctrl.position = {name: 'Position X', timing: true, active: true, canStart: false, showAllEntries: false};
            ctrl.addPositionForm = {
                $setPristine: function() {},
                $setUntouched: function() {}
            };

            expect(ctrl.positions.length).toBe(2);
            ctrl.addPosition(ctrl.position);
            deferredSave.resolve({id: 3});
            scope.$apply();

            expect(positionService.addPosition).toHaveBeenCalled();
            expect(ctrl.position.name).toBeUndefined();
            expect(ctrl.position.timing).toBe(false);
            expect(ctrl.position.active).toBe(false);
            expect(ctrl.position.canStart).toBe(false);
            expect(ctrl.position.showAllEntries).toBe(false);
        });

        it('should call create position but throw error', function() {
            getPositions();
            ctrl.position = {name: 'Position X', timing: true, active: true, canStart: false, showAllEntries: false};
            expect(ctrl.positions.length).toBe(2);
            ctrl.addPosition(ctrl.position);
            deferredSave.reject();
            scope.$apply();

            expect(positionService.addPosition).toHaveBeenCalled();
            expect(ctrl.position.name).toBe('Position X');
            expect(ctrl.position.timing).toBe(true);
            expect(ctrl.position.active).toBe(true);
            expect(ctrl.position.canStart).toBe(false);
            expect(ctrl.position.showAllEntries).toBe(false);
            expect(alertService.getAlert().msg).toBe('FAILED_ADD');
        });

        it('should call delete position', function() {
            ctrl.deletePosition({id: 1});
            expect(positionService.removePosition).toHaveBeenCalled();
            deferredRemove.resolve();
            scope.$apply();
        });

        it('should call delete position but throw error', function() {
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.deletePosition({id: 1});
            expect(positionService.removePosition).toHaveBeenCalled();
            deferredRemove.reject();
            scope.$apply();
            expect(alertService.getAlert().msg).toBe('FAILED_DELETE');
        });

        it('should call update position', function() {
            ctrl.updatePosition({id: 1});
            expect(positionService.updatePosition).toHaveBeenCalled();
            deferredSave.resolve();
            scope.$apply();
        });

        it('should call update position but throw error', function() {
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.updatePosition({id: 1});
            expect(positionService.updatePosition).toHaveBeenCalled();
            deferredSave.reject();
            scope.$apply();
            expect(alertService.getAlert().msg).toBe('FAILED_UPDATE');
        });
    });
});