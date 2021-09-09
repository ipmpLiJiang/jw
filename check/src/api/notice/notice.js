import fetch from '@/utils/fetch'


//查询登录提示
export function getDetail(data) {
    return fetch({
        url: 'logintips/list',
        method: 'post',
        data:data
    })
}
//修改登录提示
export function updateNotice(data) {
    return fetch({
        url: 'logintips/update',
        method: 'post',
        data: data
    })
}
