import fetch from '@/utils/fetch'

//个人评估报告接口
export function getUserDetail(data) {
    return fetch({
        url: 'evaluation/list',
        method: 'post',
        data:data
    })
}

//个人评估综合结果详情接口
export function getSingleTotalScore(data) {
    return fetch({
        url: 'resultdetail/getSingleTotalScore',
        method: 'post',
        data:data
    })
}

//组织部查看所有人员测评报告接口
export function selectAllReport(data) {
    return fetch({
        url: 'evaluation/selectAllReport',
        method: 'post',
        data:data
    })
}

//部门长查看该部门下人员测评报告接口
export function selectDeptReport() {
    return fetch({
        url: 'evaluation/selectDeptReport',
        method: 'post',
    })
}
//获取给被评分人打分的评分人和分数接口
export function getScorringAndScore(data) {
    return fetch({
        url: 'resultdetail/getScorringAndScore',
        method: 'post',
        data:data
    })
}

