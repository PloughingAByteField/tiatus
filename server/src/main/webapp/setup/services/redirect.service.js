(function() {
    angular.module('RedirectService', []).factory('Redirect', ['$window',
      function($window){
        return {
            redirect : function(url) {
                $window.location.href = url;
            }
        }
    }]);
})();

