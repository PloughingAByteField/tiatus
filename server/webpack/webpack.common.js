/**
 * @author: @AngularClass
 */

const webpack = require('webpack');
const helpers = require('./helpers');

/*
 * Webpack Plugins
 */
// problem with copy-webpack-plugin
const AssetsPlugin = require('assets-webpack-plugin');
const NormalModuleReplacementPlugin = require('webpack/lib/NormalModuleReplacementPlugin');
const ContextReplacementPlugin = require('webpack/lib/ContextReplacementPlugin');
const CommonsChunkPlugin = require('webpack/lib/optimize/CommonsChunkPlugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const CheckerPlugin = require('awesome-typescript-loader').CheckerPlugin;
const HtmlElementsPlugin = require('./html-elements-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const LoaderOptionsPlugin = require('webpack/lib/LoaderOptionsPlugin');
const ScriptExtHtmlWebpackPlugin = require('script-ext-html-webpack-plugin');

/*
 * Webpack Constants
 */
const HMR = helpers.hasProcessFlag('hot');
const AOT = helpers.hasNpmFlag('aot');
const METADATA = {
  title: 'Tiatus Timing System',
  baseUrl: '/',
  isDevServer: helpers.isWebpackDevServer()
};

const TARGET = process.env.TARGET;
const COMPILED_DIR='../target/ts_compiled';

/*
 * Webpack configuration
 *
 * See: http://webpack.github.io/docs/configuration.html#cli
 */
module.exports = function (options) {
  isProd = options.env === 'production';
  return {

    /*
     * Cache generated modules and chunks to improve performance for multiple incremental builds.
     * This is enabled by default in watch mode.
     * You can pass false to disable it.
     *
     * See: http://webpack.github.io/docs/configuration.html#cache
     */
    //cache: false,

    /*
     * The entry point for the bundle
     * Our Angular.js app
     *
     * See: http://webpack.github.io/docs/configuration.html#entry
     */
    entry: {
      'polyfills': './src/main/typescript/polyfills.browser.ts',
      'results':  isProd ? './src/main/typescript/results/main.aot.ts' : './src/main/typescript/results/main.ts',
      'timing':  isProd ? './src/main/typescript/timing/main.aot.ts' : './src/main/typescript/timing/main.ts',
      'adjudicator': isProd ? './src/main/typescript/adjudicator/main.aot.ts' : './src/main/typescript/adjudicator/main.ts',
      'admin': isProd ? './src/main/typescript/admin/main.aot.ts' : './src/main/typescript/admin/main.ts',
      'setup': isProd ? './src/main/typescript/setup/main.aot.ts' : './src/main/typescript/setup/main.ts',
      'login': isProd ? './src/main/typescript/login/main.aot.ts' : './src/main/typescript/login/main.ts'
    },

    /*
     * Options affecting the resolving of modules.
     *
     * See: http://webpack.github.io/docs/configuration.html#resolve
     */
    resolve: {

      /*
       * An array of extensions that should be used to resolve modules.
       *
       * See: http://webpack.github.io/docs/configuration.html#resolve-extensions
       */
      extensions: ['.ts', '.js', '.json'],

      // An array of directory names to be resolved to the current directory
      modules: isProd ? [ helpers.root(COMPILED_DIR + '/src'), helpers.root(COMPILED_DIR + '/node_modules'), helpers.root('node_modules')] : [helpers.root('src'), helpers.root('node_modules')],

    },

    /*
     * Options affecting the normal modules.
     *
     * See: http://webpack.github.io/docs/configuration.html#module
     */
    module: {

      rules: [

        /*
         * Typescript loader support for .ts and Angular 2 async routes via .async.ts
         * Replace templateUrl and stylesUrl with require()
         *
         * See: https://github.com/s-panferov/awesome-typescript-loader
         * See: https://github.com/TheLarkInn/angular2-template-loader
         */
        {
          test: /\.ts$/,
          use: [
            '@angularclass/hmr-loader?pretty=' + !isProd + '&prod=' + isProd,
            'awesome-typescript-loader?{configFileName: "tsconfig.webpack.json"}',
            'angular2-template-loader',
            'angular-router-loader?loader=system&genDir=' + COMPILED_DIR + '&aot=' + AOT
          ],
          exclude: [/\.(spec|e2e)\.ts$/]
        },

        /*
         * Json loader support for *.json files.
         *
         * See: https://github.com/webpack/json-loader
         */
        {
          test: /\.json$/,
          use: 'json-loader'
        },

        /*
         * to string and css loader support for *.css files
         * Returns file content as string
         *
         */
        {
          test: /\.css$/,
          use: ['to-string-loader', 'css-loader']
        },

        /* Raw loader support for *.html
         * Returns file content as string
         *
         * See: https://github.com/webpack/raw-loader
         */
        {
          test: /\.html$/,
          use: 'raw-loader',
          exclude: [
            helpers.root('src/main/typescript/results/index.html'),
            helpers.root('src/main/typescript/timing/index.html'),
            helpers.root('src/main/typescript/adjudicator/index.html'),
            helpers.root('src/main/typescript/admin/index.html'),
            helpers.root('src/main/typescript/setup/index.html'),
            helpers.root('src/main/typescript/login/index.html')
          ]
        },

        /* File loader for supporting images, for example, in CSS files.
         */
        {
          test: /\.(jpg|png|gif)$/,
          use: 'file-loader'
        },

        {
            test: /\.scss$/,
            loaders: ['raw-loader', 'sass-loader']
    },
    {
              test: /\.(woff2?|ttf|eot|svg)$/,
              loader: 'url?limit=10000'
    },
      ],

    },

    /*
     * Add additional plugins to the compiler.
     *
     * See: http://webpack.github.io/docs/configuration.html#plugins
     */
    plugins: [
      new AssetsPlugin({
        path: TARGET,
        filename: 'webpack-assets.json',
        prettyPrint: true
      }),

      /*
       * Plugin: ForkCheckerPlugin
       * Description: Do type checking in a separate process, so webpack don't need to wait.
       *
       * See: https://github.com/s-panferov/awesome-typescript-loader#forkchecker-boolean-defaultfalse
       */
      new CheckerPlugin(),
      /*
       * Plugin: CommonsChunkPlugin
       * Description: Shares common code between the pages.
       * It identifies common modules and put them into a commons chunk.
       *
       * See: https://webpack.github.io/docs/list-of-plugins.html#commonschunkplugin
       * See: https://github.com/webpack/docs/wiki/optimization#multi-page-app
       */
      new CommonsChunkPlugin({
        name: 'polyfills',
        chunks: ['polyfills'],
        filename: 'polyfills/polyfills.bundle.js'
      }),
      // This enables tree shaking of the vendor modules
      new CommonsChunkPlugin({
        name: 'vendor',
        chunks: ['results', 'timing', 'adjudicator', 'admin', 'setup', 'login'],
        filename: 'vendor/vendor.bundle.js',
        minChunks: (module) => /node_modules\//.test(module.resource)
      }),
      new CommonsChunkPlugin({
        name: 'common',
        filename: '[name]/[chunkhash].bundle.js',
        chunks: ['results', 'timing', 'adjudicator', 'admin'],
        minChunks: (module, count) => /src\//.test(module.resource) && count > 1
      }),
      // Specify the correct order the scripts will be injected in
      new CommonsChunkPlugin({
        name: ['polyfills', 'vendor', 'common'].reverse()
      }),

      /**
       * Plugin: ContextReplacementPlugin
       * Description: Provides context to Angular's use of System.import
       *
       * See: https://webpack.github.io/docs/list-of-plugins.html#contextreplacementplugin
       * See: https://github.com/angular/angular/issues/11580
       */
      new ContextReplacementPlugin(
        // The (\\|\/) piece accounts for path separators in *nix and Windows
        /angular(\\|\/)core(\\|\/)@angular/,
        helpers.root('src/main/typescript'), // location of your src
        {
          // your Angular Async Route paths relative to this root directory
        }
      ),

      /*
       * Plugin: CopyWebpackPlugin
       * Description: Copy files and directories in webpack.
       *
       * Copies project static assets.
       *
       * See: https://www.npmjs.com/package/copy-webpack-plugin
       */
      new CopyWebpackPlugin([
        { from: 'node_modules/bootstrap/dist/css', to: 'assets/css' },
        { from: 'node_modules/font-awesome/css', to: 'assets/css' },
        { from: 'node_modules/font-awesome/fonts', to: 'assets/fonts' },
        { from: 'src/main/typescript/assets', to: 'assets' },
        { from: 'src/main/typescript/results/i18n', to: 'results/i18n' },
        { from: 'src/main/typescript/timing/i18n', to: 'timing/i18n' },
        { from: 'src/main/typescript/adjudicator/i18n', to: 'adjudicator/i18n' },
        { from: 'src/main/typescript/admin/i18n', to: 'admin/i18n' },
        { from: 'src/main/typescript/setup/i18n', to: 'setup/i18n' },
        { from: 'src/main/typescript/login/i18n', to: 'login/i18n' }
      ]),


      /*
       * Plugin: HtmlWebpackPlugin
       * Description: Simplifies creation of HTML files to serve your webpack bundles.
       * This is especially useful for webpack bundles that include a hash in the filename
       * which changes every compilation.
       *
       * See: https://github.com/ampedandwired/html-webpack-plugin
       */
      new HtmlWebpackPlugin({
        template: 'src/main/typescript/results/index.html',
        title: METADATA.title,
        chunksSortMode: 'dependency',
        metadata: METADATA,
        inject: 'head',
        chunks: ['results', 'polyfills', 'vendor', 'common'],
        filename: 'results/index.html'
      }),

      new HtmlWebpackPlugin({
        template: 'src/main/typescript/timing/index.html',
        title: METADATA.title,
        chunksSortMode: 'dependency',
        metadata: METADATA,
        inject: 'head',
        chunks: ['timing', 'polyfills', 'vendor', 'common'],
        filename: 'timing/index.html'
      }),

      new HtmlWebpackPlugin({
          template: 'src/main/typescript/adjudicator/index.html',
          title: METADATA.title,
          chunksSortMode: 'dependency',
          metadata: METADATA,
          inject: 'head',
          chunks: ['adjudicator', 'polyfills', 'vendor', 'common'],
          filename: 'adjudicator/index.html'
      }),

      new HtmlWebpackPlugin({
          template: 'src/main/typescript/admin/index.html',
          title: METADATA.title,
          chunksSortMode: 'dependency',
          metadata: METADATA,
          inject: 'head',
          chunks: ['admin', 'polyfills', 'vendor', 'common'],
          filename: 'admin/index.html'
      }),

      new HtmlWebpackPlugin({
          template: 'src/main/typescript/setup/index.html',
          title: METADATA.title,
          chunksSortMode: 'dependency',
          metadata: METADATA,
          inject: 'head',
          chunks: ['setup', 'polyfills', 'vendor', 'common'],
          filename: 'setup/index.html'
      }),

      new HtmlWebpackPlugin({
          template: 'src/main/typescript/login/index.html',
          title: METADATA.title,
          chunksSortMode: 'dependency',
          metadata: METADATA,
          inject: 'head',
          chunks: ['login', 'polyfills', 'vendor', 'common'],
          filename: 'login/index.html'
      }),

      /*
       * Plugin: ScriptExtHtmlWebpackPlugin
       * Description: Enhances html-webpack-plugin functionality
       * with different deployment options for your scripts including:
       *
       * See: https://github.com/numical/script-ext-html-webpack-plugin
       */
      new ScriptExtHtmlWebpackPlugin({
        defaultAttribute: 'defer'
      }),

      /*
       * Plugin: HtmlElementsPlugin
       * Description: Generate html tags based on javascript maps.
       *
       * If a publicPath is set in the webpack output configuration, it will be automatically added to
       * href attributes, you can disable that by adding a "=href": false property.
       * You can also enable it to other attribute by settings "=attName": true.
       *
       * The configuration supplied is map between a location (key) and an element definition object (value)
       * The location (key) is then exported to the template under then htmlElements property in webpack configuration.
       *
       * Example:
       *  Adding this plugin configuration
       *  new HtmlElementsPlugin({
       *    headTags: { ... }
       *  })
       *
       *  Means we can use it in the template like this:
       *  <%= webpackConfig.htmlElements.headTags %>
       *
       * Dependencies: HtmlWebpackPlugin
       */
      new HtmlElementsPlugin({
        headTags: require('./head-config.common')
      }),

      /**
       * Plugin LoaderOptionsPlugin (experimental)
       *
       * See: https://gist.github.com/sokra/27b24881210b56bbaff7
       */
      new LoaderOptionsPlugin({}),

      // Fix Angular 2
      new NormalModuleReplacementPlugin(
        /facade(\\|\/)async/,
        helpers.root('node_modules/@angular/core/src/facade/async.js')
      ),
      new NormalModuleReplacementPlugin(
        /facade(\\|\/)collection/,
        helpers.root('node_modules/@angular/core/src/facade/collection.js')
      ),
      new NormalModuleReplacementPlugin(
        /facade(\\|\/)errors/,
        helpers.root('node_modules/@angular/core/src/facade/errors.js')
      ),
      new NormalModuleReplacementPlugin(
        /facade(\\|\/)lang/,
        helpers.root('node_modules/@angular/core/src/facade/lang.js')
      ),
      new NormalModuleReplacementPlugin(
        /facade(\\|\/)math/,
        helpers.root('node_modules/@angular/core/src/facade/math.js')
      ),
    ],

    /*
     * Include polyfills or mocks for various node stuff
     * Description: Node configuration
     *
     * See: https://webpack.github.io/docs/configuration.html#node
     */
    node: {
      global: true,
      crypto: 'empty',
      process: true,
      module: false,
      clearImmediate: false,
      setImmediate: false
    }

  };
}
