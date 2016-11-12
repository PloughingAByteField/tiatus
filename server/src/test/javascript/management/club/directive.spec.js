describe("src.test.javascript.management.club.directive.spec.js", function() {

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

    describe('existing club name directive', function(){
        var $compile, $rootScope, html, elem, compiled;

          // Store references to $rootScope and $compile
          // so they are available to all tests in this describe block
          beforeEach(function() {
            html ='<form name="form"><input type="text" name="name" id="name" ng-model="ctrl.club.club" not-existing-club-name /></form>';

            inject(function(_$compile_, _$rootScope_){
                // The injector unwraps the underscores (_) from around the parameter names when matching
                $compile = _$compile_;
                $rootScope = _$rootScope_;
                scope = $rootScope.$new();
                elem = angular.element(html);
                compiled = $compile(elem);
                compiled(scope);
                scope.$digest();
              })
          });

          it('to get existing club name error', function() {
            scope.ctrl = {};
            scope.ctrl.clubs = [{id: 1, club: 'Club 1'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.club = {};
            scope.ctrl.club.club = scope.ctrl.clubs[0].club;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(false);
            expect(form.name.$error.clubNameExists).toBe(true);
          });

          it('to not get existing event name error', function() {
            scope.ctrl = {};
            scope.ctrl.clubs = [{id: 1, club: 'Club 1'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.club = {};
            scope.ctrl.club.club = 'Club 2';
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(true);
            expect(form.name.$error.clubNameExists).toBeUndefined();
          });
    });
});
