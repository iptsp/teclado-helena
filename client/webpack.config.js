const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    entry: './src/index.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist'),
    },
    mode: 'development',
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader'],
            },
        ],
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: 'src/index.html'
        }),
        new CopyWebpackPlugin({
            patterns: [
                {
                    from: 'src/audio',
                    to: 'audio',
                },
                {
                    from: 'src/images',
                    to: 'images',
                },
                {
                    from: 'src/dataset',
                    to: 'dataset'
                },
                {
                    from: 'src/manifest.json',
                    to: 'manifest.json',
                }
            ],
        }),
    ],
    devServer: {
        watchFiles: ["./src/**/*"],
        port: 9000,
        open: true,
        hot: true,
    },
};