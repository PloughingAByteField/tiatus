describe("src.test.javascript.setup.controller.js", function() {

    beforeEach(module('setupApp', function ($provide, $translateProvider) {
        $provide.factory('customLoader', function ($q) {
            return function () {
              var deferred = $q.defer();
              deferred.resolve({});
              return deferred.promise;
            };
        });

        $translateProvider.useLoader('customLoader');
    }));

    describe('userController', function(){
        describe('addUser', function() {
            var scope, deferred, User;

            beforeEach(inject(function($rootScope, $controller, $q, _User_) {
                User = _User_;
                deferredSave = $q.defer();
                spyOn(User, 'save').and.returnValue({ $promise: deferredSave.promise });
                scope = $rootScope.$new();
                ctrl = $controller('userController', {$scope: scope, User: User});
            }));

            it("should create user", function() {
               var user = {};
               user.user_name = 'username';
               user.password = "password";

               ctrl.addUserForm = {
                   $setPristine: function() {},
                   $setUntouched: function() {}
               }

            });
        });
    });
});