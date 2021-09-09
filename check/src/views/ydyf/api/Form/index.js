import fetch from '@/utils/fetch'


//人员信息查询接口
export function Formlist(data) {
    return fetch({
        url: 'evaluationNonClinical/initInfo/'+data,
        method: 'post',
       
    })
}


//自评提交接口
export function mySbmit(data) {
    return fetch({
        url: '/evaluationNonClinical/updateById',
        method: 'post',
        data:data
    })
}


//临床人员信息接口
export function initInfo(data) {
    return fetch({
        url: 'evaluationClinical/initInfo/'+data,
        method: 'post',
    
    })
}


//临床考核自我表单第一步提交
export function updateBaseInfo(data) {
    return fetch({
        url: 'evaluationClinical/updateBaseInfo',
        method: 'post',
        data:data
    
    })
}

//临床考核自我表单第二步提交
export function updateSelfSummary(data) {
    return fetch({
        url: 'evaluationClinical/updateSelfSummary',
        method: 'post',
        data:data
    
    })
}

////临床考核自我表单第三部提交
export function submitScore(data) {
    return fetch({
        url: 'evaluationClinical/submitScore',
        method: 'post',
        data:data
    
    })
}

//临床考核第三部更新
export function initFormData(data) {
    return fetch({
        url: 'evaluationClinical/initFormData/'+data,
        method: 'post',
       
    
    })
}

//临床人员已提交信息查询

export function baseInfo(data) {
    return fetch({
        url: 'evaluationClinical/baseInfo',
        method: 'post',
        data:data
    
    })
}

//获取用户评分列表

export function getScore(data) {
    return fetch({
        url: 'evaluationClinical/getScore',
        method: 'post',
        data:data
    
    })
}

//待考核人员
export function notFilled(data) {
    return fetch({
        url: 'medicalEthicsMsg/checkList',
        method: 'post',
        data:data
    
    })
}
//暂存总结
export function updateSelfSummaryTemp(data) {
    return fetch({
        url: 'evaluationClinical/updateSelfSummaryTemp',
        method: 'post',
        data:data
    
    })
}


//获取评优比例
export function ProfiCiency(data) {
    return fetch({
        url: 'calculate/currentExcellentInfo?branchId='+data,
        method: 'get',
    })
}

//重新考核人员

export function anew(data) {
    return fetch({
        url: '/medicalEthicsMsg/resubmitScore',
        method: 'post',
        data:data
    })
}

//非临床编辑打分接口
export function editById(data) {
    return fetch({
        url: '/evaluationNonClinical/editById',
        method: 'post',
        data:data
    })
}
//临床编辑打分接口
export function reScore(data) {
    return fetch({
        url: '/evaluationClinical/reScore',
        method: 'post',
        data:data
    })
}
