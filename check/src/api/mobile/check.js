import fetch from '@/utils/fetch'
//移动端考核详情
export function mobilGetList(data) {
    return fetch({
        url: '/mobile/list',
        method: 'post',
        data: data
    })
}
//移动端考核详情
export function mobilGetDetail(data) {
    return fetch({
        url: '/mobile/getDetail',
        method: 'post',
        data: data
    })
}
//提交考核
export function getTotalScore(data) {
    return fetch({
        url: '/mobile/getTotalScore',
        method: 'post',
        data: data
    })
}

//移动端个人评估报告接口
export function evaluationReport(data) {
    return fetch({
        url: '/mobile/evaluationReport',
        method: 'post',
        data: data
    })
}
//移动端综合结果详情接口
export function mobileGetSingleTotalScore(data) {
    return fetch({
        url: '/mobile/getSingleTotalScore',
        method: 'post',
        data: data
    })
}
//移动端查询个人信息
export function findUserByUserCode(data) {
    return fetch({
        url: '/mobile/findUserByUserCode',
        method: 'post',
        data: data
    })
}
//移动端修改密码
export function updateUserPassword(data) {
    return fetch({
        url: '/mobile/updateUserPassword',
        method: 'post',
        data: data
    })
}