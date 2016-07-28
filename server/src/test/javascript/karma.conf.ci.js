
var baseConfig = require('./karma.conf.dev.js');

module.exports = function(config) {
    // Load base config
    baseConfig(config);

    // Override base config
    config.set({
        browsers: ['PhantomJS'],
        singleRun: true,
        autoWatch: false
    });
};
