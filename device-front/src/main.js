import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui'
import "element-ui/lib/theme-chalk/index.css"
import "font-awesome/css/font-awesome.min.css"
import router from './router/index'
import axios from 'axios'
import store from './store/index'
// import './mock'
import './permission'
// 引入vue-amap
// import AMap from 'vue-amap';

// Vue.use(AMap);
// // 初始化vue-amap
// AMap.initAMapApiLoader({
//     // 申请的高德key
//     key: 'ebb04fd06825682d9f08a9cd3ccca6f7',
//     // 插件集合
//     plugin: [],
//     uiVersion: '1.0'  //添加 uiVersion 的脚本版本号
// });

axios.defaults.baseURL = 'http://localhost:8088'
Vue.prototype.$http = axios
Vue.config.productionTip = false
Vue.use(ElementUI)//全局注册elementUI
new Vue({
  render: h => h(App),
  router:router,
  store:store
}).$mount('#app')
