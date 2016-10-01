describe("src.test.javascript.management.race.alert.js", function() {

    beforeEach(module('managementApp'));

    describe('closeAlert', function(){
            beforeEach(inject(function($rootScope, $controller) {
                scope = $rootScope.$new();
                ctrl = $controller('raceController', {$scope: scope});
                ctrl.alert = { type: 'danger', msg: 'I am an alert' };
            }));

            it('should close alert', function() {
                expect(ctrl.alert).not.toBeUndefined();
                expect(ctrl.alert.type).toBe('danger');
                expect(ctrl.alert.msg).toBe('I am an alert');
                ctrl.closeAlert();
                expect(ctrl.alert).toBe(null);
            });
    });
});