import fetch from '@/utils/fetch'


//查询人员列表
export function getList(data) {
    return fetch({
        url: 'user/list',
        method: 'post',
        data: data
    })
}

//查询指标打分人员列表
export function getDutyScorringUserlist(data) {
    return fetch({
        url: 'user/dutyScorringUserlist',
        method: 'post',
        data: data
    })
}

//添加人员
export function addUser(data) {
    return fetch({
        url: 'user/add',
        method: 'post',
        data: data
    })
}

//考核用户列表
export function getUserByScoreFlow(data) {
    return fetch({
        url: 'user/getUserByScoreFlow',
        method: 'post',
        data: data
    })
}

//修改人员
export function updateUser(data) {
    return fetch({
        url: 'user/update',
        method: 'post',
        data: data
    })
}

//删除人员
export function deleteUser(data) {
    return fetch({
        url: 'user/delete',
        method: 'post',
        data: data
    })
}
//人员列表级联查询
export function treelist() {
    return fetch({
        url: 'usertree/treelist',
        method: 'post'
    })
}
//通过发薪号查找hrp用户数据接口
export function findHrpUserById(data) {
    return fetch({
        url: 'hrpUser/findHrpUserById',
        method: 'post',
        data:data
    })
}
//重置密码
export function resetPassword(data) {
    return fetch({
        url: 'user/resetPassword',
        method: 'post',
        data:data
    })
}
//评分关系人导出
export function exportExcel(data) {
    return fetch({
        url: 'user/exportExcel',
        method: 'post',
        data:data
    })
}
