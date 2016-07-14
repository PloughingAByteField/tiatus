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
    jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
      consolidateAll: true,
      savePath: 'target/jsunit',
      filePrefix: 'protractor'
    }));
  }
};