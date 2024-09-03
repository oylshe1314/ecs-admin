import {createApp} from 'vue'
import App from './App.vue'

import elementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElIcons from '@element-plus/icons-vue'

import router from './router'
import store from "./store";

const app = createApp(App)

app.use(elementPlus)

app.config.globalProperties.$icons = []
for (const [name, component] of Object.entries(ElIcons)) {
    app.component(name, component)
    app.config.globalProperties.$icons.push(name)
}

app.use(router)
app.use(store)

app.mount('#app')
