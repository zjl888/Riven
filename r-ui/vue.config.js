const { defineConfig } = require('@vue/cli-service')
const path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir)
}
module.exports = defineConfig({
  transpileDependencies: true,
  // 是否开启eslint保存检测，有效值：ture | false | 'error'
  lintOnSave:process.env.NODE_ENV === 'development',
  devServer:{
    host:'localhost',
    //自动打开浏览器
    open:true,
    //端口
    port:8080,
    //使用代理
    proxy:{
      [process.env.VUE_APP_BASE_API]:{
        //目标地址
        target:'http://localhost:8888',
        //配置跨域
        changeOrigin: false,
        pathRewrite: {
          //重写请求路径，目标地址中的process.env.VUE_APP_BASE_API替换为''
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        "@": resolve("src"),
      },
      fallback: {
        path: require.resolve("path-browserify"),
      },
    },
  }
})
