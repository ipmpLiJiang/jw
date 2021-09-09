import fetch from '@/utils/fetch'


//查询指标列表
export function getList(data) {
    return fetch({
        url: 'duty/list',
        method: 'post',
        data: data
    })
}

//添加指标
export function addDuty(data) {
    return fetch({
        url: 'duty/add',
        method: 'post',
        data: data
    })
}

//修改指标
export function updateDuty(data) {
    return fetch({
        url: 'duty/update',
        method: 'post',
        data: data
    })
}

//删除指标
export function deleteDuty(data) {
    return fetch({
        url: 'duty/delete',
        method: 'post',
        data: data
    })
}

//岗位列表
export function postList() {
    return fetch({
        url: "tree/treelist",
        method: 'post'
    })
}