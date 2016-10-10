(function() {
    'use strict'; // NOSONAR

    angular.module('RaceService', ['ngResource']).factory('Race', ['$resource',
      function($resource){
        return $resource('../rest/races/:id', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            }
        });
    }]);
})();
