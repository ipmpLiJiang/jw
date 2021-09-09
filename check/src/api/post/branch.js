import fetch from '@/utils/fetch'


//查询支部列表
export function treelist(data) {
    return fetch({
        url: 'branch/treelist',
        method: 'post',
        data: data
    })
}

//添加支部
export function addBranch(data) {
    return fetch({
        url: 'branch/add',
        method: 'post',
        data: data
    })
}

//修改支部
export function updateBranch(data) {
    return fetch({
        url: 'branch/update',
        method: 'post',
        data: data
    })
}

//删除支部
export function deleteBranch(data) {
    return fetch({
        url: 'branch/delete',
        method: 'post',
        data: data
    })
}