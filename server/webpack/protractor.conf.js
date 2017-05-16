require('ts-node/register');
var helpers = require('./helpers');

exports.config = {
  baseUrl: 'http://localhost:3000/',
  seleniumAddress: 'http://localhost:4444/wd/hub',

  noGlobals: true,
  suites: {
    setup: helpers.root('./src/main/typescript/setup/setup.e2e.ts'),
    login: helpers.root('./src/main/typescript/login/login.e2e.ts'),
    admin: [
      helpers.root('./src/main/typescript/admin/login.e2e.ts'),
      helpers.root('./src/main/typescript/admin/races/races.e2e.ts'),
      helpers.root('./src/main/typescript/admin/positions/positions.e2e.ts')
    ]
  },
  exclude: [],

  framework: 'jasmine2',

  allScriptsTimeout: 110000,

  capabilities: {
    'browserName': 'chrome',
    'acceptSslCerts': 'true'
  },

  /**
   * Angular 2 configuration
   *
   * useAllAngular2AppRoots: tells Protractor to wait for any angular2 apps on the page instead of just the one matching
   * `rootEl`
   */
   useAllAngular2AppRoots: true
};