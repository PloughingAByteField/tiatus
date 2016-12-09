describe("src.test.javascript.management.entry.directive.spec.js", function() {

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

    describe('existing entry number directive', function(){
        var $compile, $rootScope, html, elem, compiled;

          // Store references to $rootScope and $compile
          // so they are available to all tests in this describe block
          beforeEach(function() {
            html ='<form name="form"><input type="number" name="number" ng-model="ctrl.entry.number" not-existing-entry-number /></form>';

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

          it('to get existing entry number error', function() {
            scope.ctrl = {};
            scope.ctrl.entries = [
              {id: 1, number: 1, clubs: [{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}], event: {name: 'Event 1', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false},
              {id: 2, number: 2, clubs: [{id: 2, clubName: 'Club 3'}], event: {name: 'Event 2', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false}
            ];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.currentRace = {id: 1, name : 'Race 1'};
            scope.ctrl.entry = {id: 3, number: 1, race : {id: 1, name : 'Race 1'}};
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.number.$valid).toBe(false);
            expect(form.number.$error.entryNumberExists).toBe(true);
          });

          it('to not get existing entry number error', function() {
            scope.ctrl = {};
            scope.ctrl.entries = [
              {id: 1, number: 1, clubs: [{id: 1, clubName: 'Club 1'}, {id: 2, clubName: 'Club 2'}], event: {name: 'Event 1', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false},
              {id: 2, number: 2, clubs: [{id: 2, clubName: 'Club 3'}], event: {name: 'Event 2', id: 1, weighted: false}, race : {id: 1, name : 'Race 1'}, timeOnly: false}
            ];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.currentRace = {id: 1, name : 'Race 1'};
            scope.ctrl.entry = {id: 3, number: 3, race : {id: 1, name : 'Race 1'}};
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.number.$valid).toBe(true);
            expect(form.number.$error.entryNumberExists).toBeUndefined;
          });
    });
});
