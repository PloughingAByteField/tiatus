(function() {
    'use strict'; // NOSONAR

    angular.module('PositionService', ['ngResource']).factory('Position', ['$resource',
      function($resource){
        return $resource('../rest/positions/:id', {}, {
            query: {
                method: 'GET',
                isArray: true,
                cache: false
            },
            getActiveTimingPositions: {
                url: '../rest/positions/activeTiming',
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
    }]);
})();
