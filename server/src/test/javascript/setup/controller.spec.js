describe("src.test.javascript.setup.controller.spec.js", function() {

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
            var scope, deferred, User, Redirect;

            beforeEach(inject(function($rootScope, $controller, $q, _User_, _Redirect_) {
                User = _User_;
                Redirect = _Redirect_;
                deferredSave = $q.defer();
                spyOn(User, 'save').and.returnValue({ $promise: deferredSave.promise });
                spyOn(Redirect, 'redirect').and.returnValue({});
                scope = $rootScope.$new();
                ctrl = $controller('userController', {$scope: scope, User: User, Redirect: Redirect});
            }));

            it("should create user", function() {
               var user = {};
               user.user_name = 'username';
               user.password = "password";

               ctrl.addUserForm = {
                   $setPristine: function() {},
                   $setUntouched: function() {}
               }

               ctrl.addUser(user);
               deferredSave.resolve({id: 1, user_name: 'username', password: 'password'});
               scope.$apply();
               expect(User.save.calls.mostRecent().args.length).toBe(1);
               expect(User.save.calls.mostRecent().args[0].user_name).toBe("username");
               expect(User.save.calls.mostRecent().args[0].password).toBe("password");

               expect(Redirect.redirect).toHaveBeenCalledWith("/");
            });

            it("should fail to create user", function() {
               var user = {};
               user.user_name = 'username';
               user.password = "password";

               ctrl.addUserForm = {
                   $setPristine: function() {},
                   $setUntouched: function() {}
               }

               ctrl.addUser(user);
               deferredSave.reject();
               scope.$apply();
               expect(User.save.calls.mostRecent().args.length).toBe(1);
               expect(User.save.calls.mostRecent().args[0].user_name).toBe("username");
               expect(User.save.calls.mostRecent().args[0].password).toBe("password");

               expect(Redirect.redirect).not.toHaveBeenCalled();
            });
        });
    });
});