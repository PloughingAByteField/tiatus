exports.config = {

  allScriptsTimeout: 11000,
  seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: [
    '*.js'
  ],

  capabilities: {
    'browserName': 'chrome'
  },

  framework: 'jasmine2',
  onPrepare: function() {
    var jasmineReporters = require('jasmine-reporters');
    jasmine.getEnv().addReporter(new jasmineReporters.SonarXmlReporter({
      consolidateAll: true,
      savePath: 'target/js',
      filePrefix: 'protractor',
      modifySuiteName: function(nextPath) {
        var pathSplit = nextPath.split('.');
        var path = ''
        if (pathSplit.length > 2) {
            for (i = 0; i < pathSplit.length - 2; i++) {
                path = path +  pathSplit[i] + "/"
            }
            path = path + pathSplit[pathSplit.length - 2] + "." + pathSplit[pathSplit.length - 1]
        } else {
            path = nextPath
        }
        return path
      }
    }));
  }
};