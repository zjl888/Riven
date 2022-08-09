import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from "@/store";
import Element from 'element-ui'
import directive from "@/directive";

import './permission' //引入权限控制
Vue.use(Element)
Vue.use(directive)
Vue.config.productionTip = false

new Vue({
  el:'#app',
  router,
  store,
  render: h => h(App),
})
