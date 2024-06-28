const webpack = require('webpack');

module.exports = {
  transpileDependencies: true, // or specify an array of dependencies to transpile
  configureWebpack: {
    plugins: [
      new webpack.ProvidePlugin({
        'window.Quill': 'quill/dist/quill.js',
        'Quill': 'quill/dist/quill.js',
      })
    ]
  },
  devServer: {
    https: true,
    allowedHosts: [
      'frp-fun.top', // 允许访问的域名地址，即花生壳内网穿透的地址
      '.frp-fun.top'   // .是二级域名的通配符
    ],
  }
};
