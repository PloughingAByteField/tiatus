describe("src.test.javascript.management.position.directive.spec.js", function() {

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

    describe('existing position name directive', function(){
        var $compile, $rootScope, html, elem, compiled;

          // Store references to $rootScope and $compile
          // so they are available to all tests in this describe block
          beforeEach(function() {
            html ='<form name="form"><input type="text" name="name" id="name" ng-model="ctrl.position.name" not-existing-position-name /></form>';

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

          it('to get existing position name error', function() {
            scope.ctrl = {};
            scope.ctrl.positions = [{id: 1, name: 'Position 1'}, {id: 2, name: 'Position 2'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.position = {};
            scope.ctrl.position.name = scope.ctrl.positions[0].name;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(false);
            expect(form.name.$error.positionNameExists).toBe(true);
          });

          it('to not get existing position name error', function() {
            scope.ctrl = {};
            scope.ctrl.positions = [{id: 1, name: 'Position 1'}, {id: 2, name: 'Position 2'}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.position = {};
            scope.ctrl.position.name = 'Position 3';
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.name.$valid).toBe(true);
            expect(form.name.$error.positionNameExists).toBeUndefined();
          });
    });

    describe('existing position order directive', function(){
        var $compile, $rootScope, html, elem, compiled;

          // Store references to $rootScope and $compile
          // so they are available to all tests in this describe block
          beforeEach(function() {
            html ='<form name="form"><input type="number" name="order" ng-model="ctrl.position.order" not-existing-position-order /></form>';

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

          it('to get existing position order error', function() {
            scope.ctrl = {};
            scope.ctrl.positions = [{id: 1, name: 'Position 1', order: 1}, {id: 2, name: 'Position 2', order: 2}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.position = {};
            scope.ctrl.position.order = scope.ctrl.positions[0].order;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.order.$valid).toBe(false);
            expect(form.order.$error.positionOrderExists).toBe(true);
          });

          it('to not get existing position order error', function() {
            scope.ctrl = {};
            scope.ctrl.positions = [{id: 1, name: 'Position 1', order: 1}, {id: 2, name: 'Position 2', order: 2}];
            elem.triggerHandler('input');
            var form = scope.form;
            scope.ctrl.position = {};
            scope.ctrl.position.name = 'Position 3';
            scope.ctrl.position.order = 3;
            //need to trigger a digest here for the two-way binding
            scope.$digest();
            elem.triggerHandler('blur');
            //Don't need a $digest here because you call scope.$apply() within the blur in both if/else conditions
            //scope.$digest();
            expect(form.order.$valid).toBe(true);
            expect(form.order.$error.positionOrderExists).toBeUndefined();
          });
    });
});
