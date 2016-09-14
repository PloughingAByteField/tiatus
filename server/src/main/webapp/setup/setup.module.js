(function() {
    'use strict'; // NOSONAR

    angular.module('setupApp', [
        'pascalprecht.translate',
        'ngSanitize',
        'ngMessages',
        'ngCookies',
        'UserService',
        'RedirectService'
    ]);

    angular.module('setupApp').config(function($translateProvider) {
        $translateProvider.useStaticFilesLoader({
            prefix: 'lang/',
            suffix: '.json'
        });
        $translateProvider.useLocalStorage();
        $translateProvider.preferredLanguage('en');
        $translateProvider.fallbackLanguage('en');
        $translateProvider.useSanitizeValueStrategy('sanitize');
    });
})();
