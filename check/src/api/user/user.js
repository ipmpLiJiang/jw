import fetch from '@/utils/fetch'


//修改密码
export function updateUserPassword(data) {
    return fetch({
        url: 'user/updateUserPassword',
        method: 'post',
        data: data
    })
}
//通过发薪号检索用户接口
export function getUserByMoneyCard(data) {
    return fetch({
        url: 'user/getUserByMoneyCard',
        method: 'post',
        data: data
    })
}
//发送短信验证码接口
export function sendCheckCode(data) {
    return fetch({
        url: 'sendMessage/sendCheckCode',
        method: 'post',
        data: data
    })
}
//找回密码
export function updateNewPassword(data) {
    return fetch({
        url: 'user/updateNewPassword',
        method: 'post',
        data: data
    })
}