import fetch from '@/utils/fetch'

//查询评分人列表
export function getList(data) {
    return fetch({
        url: 'scoreStation/listScorred',
        method: 'post',
        data: data
    })
}
//查询被评分人列表
export function getScorringList(data) {
    return fetch({
        url: 'scoreStation/listScorring',
        method: 'post',
        data: data
    })
}

//添加评分人
export function addScore(data) {
    console.log("addScore")
    return fetch({
        url: 'scoreStation/addScorring',
        method: 'post',
        data: data
    })
}
//添加指标评分人
export function addDutyScore(data) {
    return fetch({
        url: 'scoreStation/addDutyScorring',
        method: 'post',
        data: data
    })
}
//添加被评分人
export function addScorred(data) {
    return fetch({
        url: 'scoreStation/addScorred',
        method: 'post',
        data: data
    })
}

//修改评分人
export function updateScore(data) {
    return fetch({
        url: 'scoreStation/update',
        method: 'post',
        data: data
    })
}

//修改被评分人
export function updateByScore(data) {
    return fetch({
        url: 'scoreStation/updateScore',
        method: 'post',
        data: data
    })
}
//修改权重系数
export function updateWeight(data) {
    return fetch({
        url: 'userStation/updateRatio',
        method: 'post',
        data: data
    })
}

//删除评分人
export function deleteScore(data) {
    return fetch({
        url: 'scoreStation/delete',
        method: 'post',
        data: data
    })
}
//删除评分人
export function deleteScoreDutyScorringUser(data) {
    return fetch({
        url: 'scoreStation/deleteScoreDutyScorringUser',
        method: 'post',
        data: data
    })
}
//批量删除评分人
export function batchDelete(data) {
    return fetch({
        url: 'scoreStation/batchDelete',
        method: 'post',
        data: data
    })
}
export function deleteDutyScore(data) {
    return fetch({
        url: 'scoreStation/deleteDutyScore',
        method: 'post',
        data: data
    })
}

export function shengChengScore(data) {
    return fetch({
        url: 'scoreStation/shengChengScore',
        method: 'post',
        data: data
    })
}
