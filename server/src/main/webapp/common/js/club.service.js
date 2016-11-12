(function() {
    'use strict'; // NOSONAR

    angular.module('ClubService', ['ngResource']).factory('Club', ['$resource',
      function($resource){
        return $resource('../rest/clubs/:id', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            },
            save: {
                method: 'POST',
                transformResponse: function(data, headers, status) {
                    if (status === 201) {
                        var locationParts = headers("Location").split('/');
                        var id = locationParts[locationParts.length - 1];
                        return {'id': id};
                    } else {
                        return data;
                    }
                }
            },
            update: {
                method: 'PUT',
                isArray: true
            },
        });
    }]);
})();
