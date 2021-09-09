import fetch from '@/utils/fetch'


//查询问卷列表
export function getList(data) {
    return fetch({
        url: 'surveyInfo/list',
        method: 'post',
        data: data
    })
}
//添加问卷
export function addQuestionnaire(data) {
    return fetch({
        url: 'surveyInfo/add',
        method: 'post',
        data: data
    })
}
//发布问卷
export function updatePublishStatus(data) {
    return fetch({
        url: 'surveyInfo/updatePublishStatus',
        method: 'post',
        data: data
    })
}
//软删除问卷接口
export function updateFlag(data) {
    return fetch({
        url: 'surveyInfo/updateFlag',
        method: 'post',
        data: data
    })
}
//问卷详情
export function getDetail(data) {
    return fetch({
        url: 'surveyInfo/getDetail',
        method: 'post',
        data: data
    })
}
//修改问卷
export function updateQuestionnaire(data) {
    return fetch({
        url: 'surveyInfo/update',
        method: 'post',
        data: data
    })
}
//提交答卷
export function batchInsert(data) {
    return fetch({
        url: 'answer/batchInsert',
        method: 'post',
        data: data
    })
}
//分析答卷
export function getAnswerDetail(data) {
    return fetch({
        url: 'answer/getAnswerDetail',
        method: 'post',
        data: data
    })
}
//分析填空答卷
export function getPackList(data) {
    return fetch({
        url: 'answer/getPackList',
        method: 'post',
        data: data
    })
}
//校验用户发薪号
export function verifyUser(data) {
    return fetch({
        // url: 'personnel/findUserByUId',
        url: 'personnel/getUserByMoneyCard',
        method: 'post',
        data: data
    })
}
//人事同步
export function ToUpdate() {
    return fetch({
        url: 'personnel/ToUpdate',
        method: 'post'
    })
}
//添加/修改预览
export function previewAdd(data) {
    return fetch({
        url: 'preview/add',
        method: 'post',
        data: data
    })
}
//查询预览详情
export function previewDetail(data) {
    return fetch({
        url: 'preview/getDetail',
        method: 'post',
        data: data
    })
}
//答卷选项填空分析接口
export function getOptionPackList(data) {
    return fetch({
        url: 'answer/getOptionPackList',
        method: 'post',
        data: data
    })
}
//调查问卷核对用户信息发送短信验证码
export function sendVoteCheckCode(data) {
    return fetch({
        url: 'sendMessage/sendVoteCheckCode',
        method: 'post',
        data: data
    })
}
