(function() {
    'use strict'; // NOSONAR

    angular.module('EventService', ['ngResource'])
    .factory('AssignedEvent', ['$resource',
      function($resource){
        return $resource('../rest/event/:id', {}, {
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
                method: 'POST'
            }
        });

    }]).factory('DeleteAssignedEvent', ['$resource',
      function($resource){
        return $resource('../rest/raceEvent/delete', {}, {
            remove: {
                method: 'POST'
            }
        });

    }]).factory('UnassignedEvent', ['$resource',
      function($resource){
        return $resource('../rest/raceEvent/unassigned', {}, {
            query: {
                method: 'GET',
                 isArray: true,
                 cache: false
            }
        });

    }]).factory('Event', ['$resource',
      function($resource){
        return $resource('../rest/events/:id', {}, {
        });
    }]);

})();

