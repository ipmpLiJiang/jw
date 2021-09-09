import fetch from '@/utils/fetch'


//发送考核题目接口
export function sendTopic(data) {
    return fetch({
        url: 'sendMessage/sendTopic',
        method: 'post',
        data: data
    })
}
//发送评估报告接口
export function sendEvaluationReport(data) {
    return fetch({
        url: 'sendMessage/sendEvaluationReport',
        method: 'post',
        data: data
    })
}
//给所有被评分人发送短信接口
export function sendOneMessage(data) {
    return fetch({
        url: 'sendMessage/sendOneMessage',
        method: 'post',
        data: data
    })
}
//一键发送接口
export function sendMessageAll(data) {
    return fetch({
        url: 'sendMessage/sendMessageAll',
        method: 'post',
        data:data
    })
}
//给用户发送短信接口
export function sendMessageUser(data) {
    return fetch({
        url: 'sendMessage/sendMessAgeToUser',
        method: 'post',
        data:data
    })
}
//人事处给用户发送短信接口
export function sendMessageToRaterUsers(data) {
    return fetch({
        url: 'sendMessage/sendMessageToRaterUsers',
        method: 'post',
        data:data
    })
}
