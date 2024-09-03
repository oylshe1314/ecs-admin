const {defineConfig} = require('@vue/cli-service')

const AutoImport = require('unplugin-auto-import/webpack')
const Components = require('unplugin-vue-components/webpack')
const {ElementPlusResolver} = require('unplugin-vue-components/resolvers')

module.exports = defineConfig({
    publicPath: '/admin',
    transpileDependencies: true,
    productionSourceMap: false,
    devServer: {
        proxy: {
            '/admin/api': {
                target: 'http://127.0.0.1:9000',
                changeOrigin: true,
                pathRewrite: {
                    '^/admin/api': '/admin/api'
                }
            }
        }
    },
    configureWebpack: {
        plugins: [
            AutoImport({resolvers: [ElementPlusResolver()]}),
            Components({resolvers: [ElementPlusResolver()]}),
        ]
    }
})
