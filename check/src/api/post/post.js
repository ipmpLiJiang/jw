import fetch from '@/utils/fetch'


//查询岗位列表
export function getList(data) {
    return fetch({
        url: 'station/list',
        method: 'post',
        data: data
    })
}

//添加岗位
export function addStation(data) {
    return fetch({
        url: 'station/add',
        method: 'post',
        data: data
    })
}

//修改岗位
export function updateStation(data) {
    return fetch({
        url: 'station/update',
        method: 'post',
        data: data
    })
}

//删除岗位
export function deleteStation(data) {
    return fetch({
        url: 'station/delete',
        method: 'post',
        data: data
    })
}