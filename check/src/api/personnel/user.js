import fetch from '@/utils/fetch'
//查询所有用户接口
export function getList(data) {
    return fetch({
        url: 'personnelUser/list',
        method: 'post',
        data: data
    })
}
//查询历史人事评分接口
export function getScoreList(data) {
    return fetch({
        url: 'personnel/selectHistoryPersonnelScorring',
        method: 'post',
        data: data
    })
}

//添加用户接口
export function addUser(data) {
    return fetch({
        url: 'personnelUser/add',
        method: 'post',
        data: data
    })
}

//添加用户接口
export function updateUser(data) {
    return fetch({
        url: 'personnelUser/update',
        method: 'post',
        data: data
    })
}
//删除用户接口
export function deleteUser(data) {
    return fetch({
        url: 'personnelUser/delete',
        method: 'post',
        data: data
    })
}
//用户管理-列表
export function getLeaderList(data) {
    return fetch({
        url: 'rater/list',
        method: 'post',
        data: data
    })
}
//用户管理-删除
export function delLeaderList(data) {
    return fetch({
        url: 'rater/delete',
        method: 'post',
        data: data
    })
}
//用户管理-添加
export function addLeaderList(data) {
    return fetch({
        url: 'rater/add',
        method: 'post',
        data: data
    })
}
//用户管理-修改
export function updateLeaderList(data) {
    return fetch({
        url: 'rater/update',
        method: 'post',
        data: data
    })
}
//确认上传
export function confirmImportExcel(data) {
    return fetch({
        url: 'personnelUser/confirmImportExcel',
        method: 'post',
        data: data
    })
}
//非人事部管理员确认上传分数excel接口
export function confirmImportExcelByManager(data) {
    return fetch({
        url: 'personnel/confirmImportExcelByManager',
        method: 'post',
        data: data
    })
}
//确认修改用户接口
export function confirmUpdate(data) {
    return fetch({
        url: 'personnelUser/confirmUpdate',
        method: 'post',
        data: data
    })
}

