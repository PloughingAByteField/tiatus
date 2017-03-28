(function() {
    angular.module('setupApp').controller('userController', UserController);

    function UserController($scope, $log, Setup, UploadLogo, Redirect) {
        var vm = this;

        vm.addUserForm = {};
        vm.user = {};
        vm.event = {};

        vm.setupApp = function(user, event) {
            $log.debug("got user " + user.userName + " password " + user.password);
            $log.debug(event);
            $log.debug(event.logo);

            Setup.event(event.title).$promise.then(function() {
                vm.event = {};

                if (typeof event.logo !== 'undefined') {
                    UploadLogo.save(event.logo).then(function(data) {
                        $log.debug('uploaded');

                    }, function(errResponse) {
                        $log.debug('failed to upload');
                    });
                }

                Setup.save(user).$promise.then(function() {
                    vm.user = {};
                    vm.addUserForm.$setPristine();
                    vm.addUserForm.$setUntouched();

                    Redirect.redirect("/");

                }, function(errResponse) {
                    $log.warn("Failed to add user", errResponse);
                    vm.addUserForm.$invalid = true;
                });

            }, function(errResponse) {
                $log.warn("Failed to set event title", errResponse);
            });
        };
    };
})();