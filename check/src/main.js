import Vue from 'vue'
//element-ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(ElementUI);

//vant-ui
import Vant from 'vant';
import 'vant/lib/index.css';
Vue.use(Vant);

//百度echarts图表
import echarts from 'echarts'
Vue.use(echarts)
Vue.prototype.$echarts = echarts

import App from './App.vue'
import router from './router'
import store from './store'
//阿里巴巴图标
import './assets/ali-font/iconfont.css'
//登录图形验证码
import SIdentify from './components/identify'
Vue.use(SIdentify);

//样式格式化
import './assets/css/public.css'

//图片裁剪
// import Croppa from 'vue-croppa'
// Vue.use(Croppa)

//文本编辑器
// import CKEditor from '@ckeditor/ckeditor5-vue';
// Vue.use( CKEditor );

//阿里巴巴图标
import './assets/ali-font/iconfont.css'

//打印
import Print from './views/ydyf/examine/utils/print' 
Vue.use(Print); 

//全局公共方法
// import common from './utils/common'
import common from './utils/common'
Vue.prototype.common = common;





//不需要验证的路径数据
let pathArr = ['/login', '/web', '/webShow', '/webShowSuccess', '/webShowQuestion', '/questionList', '/mobileQuestionList', '/submitSuccess', '/forgetPsd'];

//路由拦截
router.beforeEach((to, from, next) => {
    if (to.meta.title) {
        document.title = to.meta.title
    }
    let verify = true;
    pathArr.forEach((row) => {
        if (to.fullPath.indexOf(row) != -1) {
            verify = false;
        }
    })
    if (verify) {
        if (store.getters.getStorage) { // 判断是否有用户信息
            store.commit('$_setStorage', { user: store.getters.getStorage.user });
            if(!store.getters.getStorage.user.medicalEthicsRoleList){
                next({
                    path: '/login',
                })
            }
            if (to.path == "/") {
                //如果没有该字段直接退出登录
                if (!store.getters.getStorage.user.medicalEthicsRoleList) {
                    this.$router.push({ path: "/login" });
                    this.$store.commit("$_removeStorage");
                    return;
                }
                if (store.getters.getStorage.user.roleList.length > 1) {
                    next({
                        path: '/index',
                    })
                } else if (store.getters.getStorage.user.rolecode == 400 || store.getters.getStorage.user.rolecode == 500) {
                    next({
                        path: '/personnel',
                    })
                }else if(store.getters.getStorage.user.medicalEthicsRoleList.indexOf('200') != -1){
                    next({
                        path: '/ydyf',
                    })
                }else if(store.getters.getStorage.user.medicalEthicsRoleList.indexOf('101') != -1 || store.getters.getStorage.user.medicalEthicsRoleList.indexOf('300') != -1){
                    next({
                        path: '/ydyf/yesAssessment',
                    })
                } else {
                    next({
                        path: '/home',
                    })
                }
            } else {
                next();
            }
        } else {
            if (to.path == "/") {
                next();
            } else {
                next({
                    path: '/',
                    query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
                })
            }
        }
    } else {
        //判断是否是从oa进入
        if (to.path == "/login") {
            if (to.query.isoa == 1) {
                store.commit("$_removeStorage");
            }
        }
        //判断移动端登录信息

        if (store.getters.getStorage) { // 判断是否有用户信息

            store.commit('$_setStorage', { user: store.getters.getStorage.user });
            if (to.path == "/login") {
                if (store.getters.getStorage.user.roleList.length > 1) {
                    next({
                        path: '/index',
                    })
                } else if (store.getters.getStorage.user.rolecode == 400 || store.getters.getStorage.user.rolecode == 500) {
                    next({
                        path: '/personnel',
                    })
                } else {
                    next({
                        path: '/home',
                    })
                }
            } else {
                next();
            }
        } else {
            if (to.path == "/webShowLogin" || to.path == "/login" || to.path == "/questionList" || to.path == "/submitSuccess" || to.path == "/forgetPsd" || to.path == "/mobileQuestionList") {
                next();
            } else {
                if ((navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i))) {
                    next({
                        path: '/webShowLogin',
                        query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
                    })
                }
                else {
                    next({
                        path: '/',
                        query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
                    })
                }
            }
        }

    }

})


new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
