// Karma configuration
// Generated on Sun Aug 23 2015 17:49:01 GMT+0100 (IST)

module.exports = function(config) {
  config.set({

    // base path, that will be used to resolve files and exclude
    basePath: '../../..',


    // frameworks to use
    frameworks: ['jasmine'],


    // list of files / patterns to load in the browser
    files: [
        'bower_components/angular/angular.js',
        'bower_components/angular-route/angular-route.js',
        'bower_components/angular-resource/angular-resource.js',
        'bower_components/angular-messages/angular-messages.js',
        'bower_components/angular-cookies/angular-cookies.js',
        'bower_components/angular-sanitize/angular-sanitize.js',
        'bower_components/angular-translate/angular-translate.js',
        'bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.js',
        'bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
        'bower_components/angular-translate-storage-local/angular-translate-storage-local.js',
        'bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
        'bower_components/angular-drag-and-drop-lists/angular-drag-and-drop-lists.js',
        'bower_components/angular-mocks/angular-mocks.js',
        'bower_components/ng-table/dist/ng-table.js',
        'bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js',
        'src/main/webapp/setup/setup.module.js',
        'src/main/webapp/setup/**/*.js',
        'src/main/webapp/management/management.module.js',
        'src/main/webapp/management/**/*.module.js',
        'src/main/webapp/management/**/*.js',
        'src/main/webapp/common/js/**/*.js',
        'src/test/javascript/setup/*.spec.js',
        'src/test/javascript/management/race/*.js',
        'src/test/javascript/management/event/*.js'
    ],


    // list of files to exclude
    exclude: [

    ],

    sonarQubeUnitReporter: {
      outputFile: 'target/js/TESTS.xml',
      useBrowserName: false,
      filenameFormatter: function(unformattedPath) {
        var pathSplit = unformattedPath.split('.');
        var path = '';
        if (pathSplit.length > 2) {
            for (i = 0; i < pathSplit.length - 2; i++) {
                path = path +  pathSplit[i] + "/";
            }
            path = path + pathSplit[pathSplit.length - 2] + "." + pathSplit[pathSplit.length - 1];
        } else {
            path = unformattedPath;
        }

        return path;
      }
    },

    // test results reporter to use
    // possible values: 'dots', 'progress', 'junit', 'growl', 'coverage'
    reporters: ['progress', 'sonarqubeUnit', 'coverage'],

    junitReporter: {
//        useBrowserName: false ,
        outputDir: 'target/jsunit'
    },

    preprocessors: {
        'src/main/webapp/common/**/*.js': ['coverage'],
        'src/main/webapp/management/**/*.js': ['coverage'],
        'src/main/webapp/setup/**/*.js': ['coverage']
    },

    coverageReporter: {
        type: 'lcov',
        dir: 'target/coverage',
        subdir: '.'
    },

    // web server port
    port: 9876,


    // enable / disable colors in the output (reporters and logs)
    colors: true,


    // level of logging
    // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
    logLevel: config.LOG_INFO,


    // enable / disable watching file and executing tests whenever any file changes
    autoWatch: true,


    // Start these browsers, currently available:
    // - Chrome
    // - ChromeCanary
    // - Firefox
    // - Opera (has to be installed with `npm install karma-opera-launcher`)
    // - Safari (only Mac; has to be installed with `npm install karma-safari-launcher`)
    // - PhantomJS
    // - IE (only Windows; has to be installed with `npm install karma-ie-launcher`)
    browsers: ['Chrome'],


    // If browser does not capture in given timeout [ms], kill it
    captureTimeout: 60000,


    // Continuous Integration mode
    // if true, it capture browsers, run tests and exit
    singleRun: false
  });
};
