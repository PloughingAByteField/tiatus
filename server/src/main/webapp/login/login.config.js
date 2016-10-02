(function() {
    'use strict'; // NOSONAR

    angular.module('loginApp').config(function($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: 'login/lang/',
            suffix: '.json'
        });
        $translateProvider.useLocalStorage();
        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
        $translateProvider.useSanitizeValueStrategy('sanitize');
    });
})();