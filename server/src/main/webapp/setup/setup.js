angular.module('setupApp', [
    'pascalprecht.translate',
    'ngSanitize',
    'ngCookies'
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

angular.module('setupApp').controller('userController', function($scope) {
    console.log("userController");
});