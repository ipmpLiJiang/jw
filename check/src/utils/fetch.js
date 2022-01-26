import axios from 'axios'
import store from '../store'
import router from '../router'
import {
    Message
} from 'element-ui'
// import store from '../store'
// import {
//     getToken
// } from '@/utils/auth'

axios.defaults.withCredentials = true;

// 创建axios实例
const service = axios.create({
    // baseURL: process.env.VUE_APP_URL, // api的base_url
    baseURL: process.env.VUE_APP_ITEM_NAME, // api的base_url
    // baseURL: 'http://localhost:8080/',
    timeout: 600000, // 请求超时时间
})

service.interceptors.request.use(function (config) {
    // 发送请求前先检查是否有userid
    if (localStorage.getItem('check')) {
        config.headers['u_id'] = JSON.parse(localStorage.getItem('check')).user.usercode;
        // config.headers['u_id'] = 10010698;
    } 
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// respone响应拦截器
service.interceptors.response.use(
    response => {
        if (response.data.code === 810) {
            Message({
                message: response.data.msg,
                type: 'error',
                duration: 5 * 1000
            })
            store.commit("$_removeStorage");
            router.push({
                path: "/"
            });
        }
        if (response.data.code === 811) {
            Message({
                message: response.data.msg,
                type: 'error',
                duration: 5 * 1000
            })
            store.commit("$_removeStorage");
            router.push({
                path: "/"
            });
        }
        if (response.data.code === 812) {
            Message({
                message: response.data.msg,
                type: 'error',
                duration: 5 * 1000
            })
            store.commit("$_removeStorage");
            router.push({
                path: "/webShowLogin"
            });
        }
        return response

    },
    //非200状态（系统错误） 走下面 需要 catch 捕获
    error => {
        Message({
            message: '系统异常',
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default service
