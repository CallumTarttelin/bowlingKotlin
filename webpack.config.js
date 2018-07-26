/* eslint-disable no-unused-vars */

const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const path = require('path');

module.exports = {
  mode: 'development',
  entry: './src/main/js/app.jsx',
  output: {
    path: __dirname,
    filename: './src/main/resources/static/built/bundle.js',
  },
  devtool: 'source-map',
  module: {
    rules: [
      {
        enforce: 'pre',
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: 'eslint-loader',
      },
      {
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: [
              'env',
              'react',
            ],
          },
        },
      },
      {
        test: /\.scss$/,
        use: [
          // fallback to style-loader in development
          process.env.NODE_ENV === '//production' ? 'style-loader' : MiniCssExtractPlugin.loader,
          'css-loader',
          'sass-loader',
        ],
      },
    ],
  },
  plugins: [new MiniCssExtractPlugin({ filename: './src/main/resources/static/built/bundle.css', chunkFilename: './src/main/resources/static/built/[id].css' })],
  resolve: {
    extensions: ['.js', '.jsx'],
  },
};
