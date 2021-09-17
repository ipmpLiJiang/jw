import fetch from '@/utils/fetch'


//首页列表
export function getList(data) {
    return fetch({
        url: 'usersummary/list',
        method: 'post',
        data: data
    })
}
//考核详情
export function getDetail(data) {
    return fetch({
        url: 'homepage/getDetail',
        method: 'post',
        data: data
    })
}
//提交考核
export function scoring(data) {
    return fetch({
        url: 'homepage/getTotalScore',
        method: 'post',
        data: data
    })
}

//个人历史考核查询
export function queryByUser(data) {
    return fetch({
        url: 'quarter/queryByUser',
        method: 'post',
        data: data
    })
}
//考核列表
export function getScoreFlow(data) {
    return fetch({
        url: 'homepage/getScoreFlow',
        method: 'post',
        data: data
    })
}
//考核计算
export function JiSuan(data) {
    return fetch({
        url: 'homepage/jisuanScore',
        method: 'post',
        data: data
    })
}

//个人评分列表
export function selfGetList(data) {
    return fetch({
        url: 'usersummary/selectUserSummaryLike',
        method: 'post',
        data: data
    })
}
//个人评分列表
export function selfDutyGetList(data) {
    return fetch({
        url: 'usersummary/selectUserSummaryLikeDuty',
        method: 'post',
        data: data
    })
}