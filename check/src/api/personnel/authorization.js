import fetch from '@/utils/fetch'

//查询部门下所有用户接口
export function getList(data) {
    return fetch({
        url: 'authorizationUser/getAuthorizationList',
        method: 'post',
        data: data
    })
}
//查询授权历史列表
export function getHistoryList(data) {
    return fetch({
        url: 'authorizationUser/getHistoryAuthorizationList',
        method: 'post',
        data: data
    })
}
//开启授权代理人接口
export function openAuthorization(data) {
    return fetch({
        url: 'authorization/openAuthorization',
        method: 'post',
        data: data
    })
}

//修改授权代理人接口
export function updateAuthorization(data) {
    return fetch({
        url: 'authorization/updateAuthorization',
        method: 'post',
        data: data
    })
}
//取消授权代理人接口
export function closeAuthorization(data) {
    return fetch({
        url: 'authorization/closeAuthorization',
        method: 'post',
        data: data
    })
}
