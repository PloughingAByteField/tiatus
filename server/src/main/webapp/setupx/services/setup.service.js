(function() {
    angular.module('SetupService', ['ngResource']).factory('Setup', ['$resource',
      function($resource){
        return $resource('/rest/setup/user/:id', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            },
            event: {
                url: '/rest/setup/title',
                method: 'POST',
                cache: false
            }
        });
    }]);
    angular.module('SetupService').service('UploadLogo', ['$http',
        function ($http) {
            return {
                save: function(file) {
                    var fd = new FormData();
                    fd.append('file', file);
                    return $http.post("/rest/setup/logo", fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    });
                }
            }
    }]);
})();