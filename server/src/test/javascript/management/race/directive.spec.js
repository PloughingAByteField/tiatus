describe("src.test.javascript.management.race.directive.spec.js", function() {

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
            html ='<form name="form"><input type="text" name="name" id="name" ng-model="ctrl.race.name" not-existing-race-name /></form>';

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

          it('to get existing race name error', function() {
            scope.ctrl = {};
            scope.ctrl.races = [{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.race = {};
            scope.ctrl.race.name = scope.ctrl.races[0].name;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(false);
            expect(form.name.$error.raceNameExists).toBe(true);
          });

          it('to not get existing race name error', function() {
            scope.ctrl = {};
            scope.ctrl.races = [{id: 1, name: 'Race 1'}, {id: 2, name: 'Race 2'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.race = {};
            scope.ctrl.race.name = 'Race 3';
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(true);
            expect(form.name.$error.raceNameExists).toBeUndefined();
          });
    });


    describe('existing race number directive', function(){
        var $compile, $rootScope, html, elem, compiled;

          // Store references to $rootScope and $compile
          // so they are available to all tests in this describe block
          beforeEach(function() {
            html ='<form name="form"><input type="number" name="order" ng-model="ctrl.race.order" not-existing-race-number /></form>';

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

          it('to get existing race name error', function() {
            scope.ctrl = {};
            scope.ctrl.races = [{id: 1, raceOrder: 1, name: 'Race 1'}, {id: 2, raceOrder: 2, name: 'Race 2'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.race = {};
            scope.ctrl.race.order = scope.ctrl.races[0].raceOrder;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.order.$valid).toBe(false);
            expect(form.order.$error.raceNumberExists).toBe(true);
          });

          it('to not get existing race name error', function() {
            scope.ctrl = {};
            scope.ctrl.races = [{id: 1, raceOrder: 1, name: 'Race 1'}, {id: 2, raceOrder: 2, name: 'Race 2'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.race = {};
            scope.ctrl.race.order = 3;
            //need to trigger a digest here for the two-way binding
             scope.$digest();
             elem.triggerHandler('blur');
             //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
             //scope.$digest();
             expect(form.order.$valid).toBe(true);
             expect(form.order.$error.raceNumberExists).toBeUndefined();
          });
    });
});
