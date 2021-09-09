//权限接口
import fetch from '@/utils/fetch'

export function getList(data) {
    return fetch({
        url: 'permission/showAllData',
        method: 'post',
        data: data
    })
}

export function showDetails(data) {
    return fetch({
        url: 'permission/showDetails',
        method: 'post',
        data: data
    })
}

export function operation(data) {
    return fetch({
        url: 'permission/operation',
        method: 'post',
        data: data
    })
}