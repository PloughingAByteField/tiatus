(function() {
    'use strict'; // NOSONAR

    angular.module('UserService', ['ngResource']).factory('User', ['$resource',
      function($resource){
        return $resource('../rest/users/:id', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            },
            roles:  {
                url: '../rest/users/roles',
                method: 'GET',
                isArray: true,
                cache: false
            },
            update: {
                method: 'PUT',
                isArray: true
            }
        });
    }]);
})();
