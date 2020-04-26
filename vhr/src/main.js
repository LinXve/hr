// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import axios from 'axios'
import store from './store' //状态管理

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 图标库
import 'font-awesome/css/font-awesome.min.css'
// 日期格式
import '@/utils/format_utils'
//统一请求处理
import {getRequest} from '@/utils/api.js'
import {postRequest} from '@/utils/api.js'
import {putRequest} from '@/utils/api.js'
import {deleteRequest} from '@/utils/api.js'

Vue.prototype.$http=axios
Vue.prototype.getRequest= getRequest
Vue.prototype.postRequest= postRequest
Vue.prototype.putRequest= putRequest
Vue.prototype.deleteRequest= deleteRequest

Vue.config.productionTip = false
Vue.use(ElementUI)
router.beforeEach((to, from, next)=> {
    if (to.name == 'Login') {
      next();
      return;
    }
    var name = store.state.user.name;
    if (name == '未登录') {
      if (to.meta.requireAuth || to.name == null) {
        next({path: '/', query: {redirect: to.path}})
      } else {
        next();
      }
    } else {
      if(to.path=='/chat')
        store.commit("updateMsgList", []);
      next();
    }
  })

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>',
  mounted() {
    this.$store.dispatch('connect');
  }
})
