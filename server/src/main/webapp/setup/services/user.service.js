(function() {
    angular.module('UserService', ['ngResource']).factory('User', ['$resource',
      function($resource){
        return $resource('/rest/setup/user/:id', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            }
        });
    }]);
})();