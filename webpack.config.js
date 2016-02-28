
var outputDir = "./target/classes/"
var path = require('path');
var webpack = require('webpack');

module.exports = {
    entry: './src/main/resources/main.js',
    output: { path: outputDir, filename: 'app.js' },
    plugins: [
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoErrorsPlugin(),
        new webpack.DefinePlugin({
            'process.env': {
                'NODE_ENV': "'development'"
            }
        })
    ],
  module: {
    loaders: [
    {
    test: /.jsx?$/,
    loader: 'babel-loader',
    exclude: /node_modules/,
    query: {
      presets: ['es2015', 'react']
    }
    },
    {test: /\.css/, loader: 'style-loader!css-loader'},
    {test: /\.less$/, loader:  'style!css!less'},
    ]
  },
};