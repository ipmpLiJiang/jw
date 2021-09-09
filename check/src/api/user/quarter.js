import fetch from '@/utils/fetch'


//查询个人月份总结接口
export function getList(data) {
    return fetch({
        url: 'monthsummary/list',
        method: 'post',
        data: data
    })
}
//添加个人月份总结接口
export function addQuarter(data) {
    return fetch({
        url: 'monthsummary/add',
        method: 'post',
        data: data
    })
}
//修改个人月份总结接口
export function updateQuarter(data) {
    return fetch({
        url: 'monthsummary/update',
        method: 'post',
        data: data
    })
}
//上传文件
export function uploadFile(data) {
    return fetch({
        url: 'summaryattachment/upload',
        method: 'post',
        data: data
    })
}
//评分汇总修改个人月份总结接口
export function updateStateBySerialNo(data) {
    return fetch({
        url: 'quarter/updateStateBySerialNo',
        method: 'post',
        data: data
    })
}