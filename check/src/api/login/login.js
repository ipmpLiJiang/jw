import fetch from '@/utils/fetch'


//登录
export function isLogin(data) {
    return fetch({
        url: 'login',
        // url: '/api/login',
        method: 'post',
        data: data
    })
}

//oa登录
export function isLoginId(data) {
    return fetch({
        url: 'singlelogion',
        method: 'post',
        data: data
    })
}

//移动端登录
export function mobileLogin(data) {
    return fetch({
        url: 'mobile/login',
        method: 'post',
        data: data
    })
}