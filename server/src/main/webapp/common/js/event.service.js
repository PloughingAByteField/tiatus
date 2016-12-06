(function() {
    'use strict'; // NOSONAR

    angular.module('EventService', ['ngResource'])
    .factory('AssignedEvent', ['$resource',
      function($resource){
        return $resource('../rest/events/assigned/:id', {}, {
            query: {
                method: 'GET',
                isArray: true,
                cache: false
            },
            update: {
                method: 'PUT',
                isArray: true
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
            }
        });

    }]).factory('UnassignedEvent', ['$resource',
      function($resource){
        return $resource('../rest/events/unassigned', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            }
        });

    }]).factory('Event', ['$resource',
      function($resource){
        return $resource('../rest/events/:id', {}, {
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
            }
        });
    }]);

})();

