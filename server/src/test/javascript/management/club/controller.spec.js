describe("src.test.javascript.management.club.controller.spec.js", function() {

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

    describe('clubController', function(){

        var scope, deferred, clubService, alertService;

        beforeEach(inject(function($rootScope, $controller, $q, _clubService_, _alertService_, $httpBackend) {
            clubService = _clubService_;
            alertService = _alertService_;
            deferredSave = $q.defer();
            deferredQuery = $q.defer();
            deferredRemove = $q.defer();
            spyOn(clubService, 'getClubs').and.returnValue(deferredQuery.promise);
            spyOn(clubService, 'addClub').and.returnValue(deferredSave.promise);
            spyOn(clubService, 'removeClub').and.returnValue(deferredRemove.promise);
            spyOn(clubService, 'updateClub').and.returnValue(deferredSave.promise);
            spyOn(alertService, 'clearAlert').and.returnValue();
            scope = $rootScope.$new();
            ctrl = $controller('clubController', {$scope: scope, clubService: clubService, alertService: alertService});
            $httpBackend.whenGET('home/home.html').respond();
        }));

        function getClubs() {
            deferredQuery.resolve([{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}]);
            scope.$apply();
        };

        it('should get clubs', function() {
            expect(ctrl.clubs).toBeUndefined();
            getClubs();
            expect(clubService.getClubs).toHaveBeenCalled();
            expect(ctrl.clubs.length).toBe(2);
        });

        it('should get clubs but fail', function() {
            expect(ctrl.clubs).toBeUndefined();
            deferredQuery.reject();
            scope.$apply();
            expect(clubService.getClubs).toHaveBeenCalled();

        });

        it('should call clearAlert', function() {
            ctrl.closeAlert();
            expect(alertService.clearAlert).toHaveBeenCalled();
        });

        it('should call create club', function() {
            getClubs();
            ctrl.club = {clubName: 'Club 3'};
            ctrl.addClubForm = {
                $setPristine: function() {},
                $setUntouched: function() {}
            };

            expect(ctrl.clubs.length).toBe(2);
            ctrl.addClub(ctrl.club);
            deferredSave.resolve({id: 3});
            scope.$apply();

            expect(clubService.addClub).toHaveBeenCalled();
            expect(ctrl.club.club).toBeUndefined();
        });

        it('should call create club but throw error', function() {
            getClubs();
            ctrl.club = {name: 'Club 3'};
            expect(ctrl.clubs.length).toBe(2);
            ctrl.addClub(ctrl.club);
            deferredSave.reject();
            scope.$apply();

            expect(clubService.addClub).toHaveBeenCalled();
            expect(ctrl.club.name).toBe('Club 3');
            expect(alertService.getAlert().msg).toBe('FAILED_ADD');
        });

        it('should call delete club', function() {
            ctrl.removeClub({id: 1});
            expect(clubService.removeClub).toHaveBeenCalled();
            deferredRemove.resolve();
            scope.$apply();
        });

        it('should call delete club but throw error', function() {
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.removeClub({id: 1});
            expect(clubService.removeClub).toHaveBeenCalled();
            deferredRemove.reject();
            scope.$apply();
            expect(alertService.getAlert().msg).toBe('FAILED_REMOVE');
        });

        it('should call update club', function() {
            ctrl.updateClub({id: 1, clubName: 'Update'});
            expect(clubService.updateClub).toHaveBeenCalled();
            deferredSave.resolve();
            scope.$apply();
        });

        it('should call update club but throw error', function() {
            expect(alertService.getAlert().msg).toBe(null);
            ctrl.updateClub({id: 1, clubName: 'Update'});
            expect(clubService.updateClub).toHaveBeenCalled();
            deferredSave.reject();
            scope.$apply();
            expect(alertService.getAlert().msg).toBe('FAILED_UPDATE');
        });

        it('should have validate name call return false for undefined club', function() {
            expect(ctrl.validClubName()).toBe(false);
            expect(ctrl.validClubName({id: 1})).toBe(false);
        });

        it('should have validate name call return false for a club name of length less than 3', function() {
            expect(ctrl.validClubName({id: 1, clubName: '12'})).toBe(false);
        });

        it('should have validate name call return false for a club name of length greater than 254', function() {
            expect(ctrl.validClubName({id: 1, clubName: '01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'})).toBe(false);
        });

        it('should have validate name call return false for an existing club name', function() {
            getClubs();
            expect(ctrl.validClubName({id: 2, clubName: 'Club 1'})).toBe(false);
        });

        it('should have validate name call return true for an non existing club name', function() {
            getClubs();
            expect(ctrl.validClubName({id: 1, clubName: 'Club 3'})).toBe(true);
        });
    });
});