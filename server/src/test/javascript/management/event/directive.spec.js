describe("src.test.javascript.management.event.directive.spec.js", function() {

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

    describe('existing race name directive', function(){
        var $compile, $rootScope, html, elem, compiled;

          // Store references to $rootScope and $compile
          // so they are available to all tests in this describe block
          beforeEach(function() {
            html ='<form name="form"><input type="text" name="name" id="name" ng-model="ctrl.event.name" not-existing-event-name /></form>';

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

          it('to get existing event name error in assigned', function() {
            scope.ctrl = {};
            scope.ctrl.assigned = [{id: 1, events: [{event: {id: 1, name: 'Event 1'}}, {event: {id: 2, name: 'Event 2'}}]}, {id: 2, events: [{event: {id: 3, name: 'Event 3'}}, {event: {id: 4, name: 'Event 4'}}]}];
            scope.ctrl.unassigned = [{id: 5, name: 'Event 5'}, {id: 6, name: 'Event 6'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.event = {};
            scope.ctrl.event.name = scope.ctrl.assigned[0].events[0].event.name;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(false);
            expect(form.name.$error.eventNameExists).toBe(true);
          });

          it('to get existing event name error in unassigned', function() {
            scope.ctrl = {};
            scope.ctrl.assigned = [{id: 1, events: [{event: {id: 1, name: 'Event 1'}}, {event: {id: 2, name: 'Event 2'}}]}, {id: 2, events: [{event: {id: 3, name: 'Event 3'}}, {event: {id: 4, name: 'Event 4'}}]}];
            scope.ctrl.unassigned = [{id: 5, name: 'Event 5'}, {id: 6, name: 'Event 6'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.event = {};
            scope.ctrl.event.name = scope.ctrl.unassigned[0].name;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(false);
            expect(form.name.$error.eventNameExists).toBe(true);
          });

          it('to not get existing event name error', function() {
            scope.ctrl = {};
            scope.ctrl.assigned = [{id: 1, events: [{event: {id: 1, name: 'Event 1'}}, {event: {id: 2, name: 'Event 2'}}]}, {id: 2, events: [{event: {id: 3, name: 'Event 3'}}, {event: {id: 4, name: 'Event 4'}}]}];
            scope.ctrl.unassigned = [{id: 5, name: 'Event 5'}, {id: 6, name: 'Event 6'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.event = {};
            scope.ctrl.event.name = 'Event 7';
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(true);
            expect(form.name.$error.eventNameExists).toBeUndefined();
          });
    });
});
