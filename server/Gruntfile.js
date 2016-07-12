module.exports = function (grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        uglify: {
            options: {
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n'
            },
            build: {
                src: 'src/<%= pkg.name %>.js',
                dest: 'build/<%= pkg.name %>.min.js'
            }
        },
        jsbeautifier: {
            beautify: {
                src: [ 'src/main/webapp/management/js/race/controllers/race.js'],
                options: {
                    config: '.jsbeautifyrc'
                }
            },
            verify: {
                src: ['Gruntfile.js', 'src/main/webapp/management/js/race/controller/race.js'],
                options: {
                    mode: 'VERIFY_ONLY',
                    config: '.jsbeautifyrc'
                }
            }
        },
        protractor: {
            options: {
                configFile: "src/test/e2e/protractor.conf.js", args: {
                    baseUrl: grunt.option('baseUrl')
                }
            },

            all: {   // Grunt requires at least one target to run so you can simply put 'all: {}' here too.
            }
        }
    });



    // Load the plugin that provides the "uglify" task.
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-jsbeautifier');
    grunt.loadNpmTasks('grunt-protractor-runner');

    // Default task(s).
    grunt.registerTask('default', ['uglify', 'jsbeautifier:beautify']);

};
