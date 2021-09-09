import fetch from '@/utils/fetch'


//查询月节总结接口
export function getList(data) {
    return fetch({
        url: 'quarter/list',
        method: 'post',
        data: data
    })
}
//批量月结提交接口
export function updateSummarySubmitState(data) {
    return fetch({
        url: 'quarter/updateSummarySubmitState',
        method: 'post',
        data: data
    })
}
//批量修改月结评分状态接口
export function updateSummaryGradeState(data) {
    return fetch({
        url: 'quarter/updateSummaryGradeState',
        method: 'post',
        data: data
    })
}
//全部修改月节评分状态接口
export function updateSummaryGradeStateAll(data) {
    return fetch({
        url: 'quarter/updateSummaryGradeStateAll',
        method: 'post',
        data: data
    })
}
//是否确认开启新的月度考核接口
export function isAllFinish(data) {
    return fetch({
        url: 'manualSetTime/isAllFinish',
        method: 'post',
        data: data
    })
}
//开启手动考核按钮接口
export function openManualAssessment(data) {
    return fetch({
        url: 'manualSetTime/openManualAssessment',
        method: 'post',
        data:data
    })
}
//修改手动考核设置时间
export function updateManualAssessment(data) {
    return fetch({
        url: 'manualSetTime/updateManualAssessment',
        method: 'post',
        data:data
    })
}
//恢复自动考核按钮接口
export function closeManualAssessment(data) {
    return fetch({
        url: 'manualSetTime/closeManualAssessment',
        method: 'post',
        data:data
    })
}