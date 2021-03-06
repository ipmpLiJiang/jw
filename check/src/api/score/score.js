import fetch from '@/utils/fetch'

//查询评分人列表
export function getList(data) {
    return fetch({
        url: 'score/listScorred',
        method: 'post',
        data: data
    })
}
//查询被评分人列表
export function getScorringList(data) {
    return fetch({
        url: 'score/listScorring',
        method: 'post',
        data: data
    })
}

//添加评分人
export function addScore(data) {
    return fetch({
        url: 'score/addScorring',
        method: 'post',
        data: data
    })
}
//添加指标评分人
export function addDutyScore(data) {
    return fetch({
        url: 'score/addDutyScorring',
        method: 'post',
        data: data
    })
}
//添加被评分人
export function addScorred(data) {
    return fetch({
        url: 'score/addScorred',
        method: 'post',
        data: data
    })
}

//修改评分人
export function updateScore(data) {
    return fetch({
        url: 'score/update',
        method: 'post',
        data: data
    })
}

//修改被评分人
export function updateByScore(data) {
    return fetch({
        url: 'score/updateScore',
        method: 'post',
        data: data
    })
}
//修改权重系数
export function updateWeight(data) {
    return fetch({
        url: 'user/updateRatio',
        method: 'post',
        data: data
    })
}

//修改权重系数
export function updateStationWeight(data) {
    return fetch({
        url: 'user/updateStationRatio',
        method: 'post',
        data: data
    })
}

//查询指标打分人员列表
export function getScoreDutyScorringUserlist(data) {
    return fetch({
        url: 'score/dutyScoreScorringUserlist',
        method: 'post',
        data: data
    })
}

//删除评分人
export function deleteScore(data) {
    return fetch({
        url: 'score/delete',
        method: 'post',
        data: data
    })
}
//删除评分人
export function deleteScoreDutyScorringUser(data) {
    return fetch({
        url: 'score/deleteScoreDutyScorringUser',
        method: 'post',
        data: data
    })
}
//批量删除评分人
export function batchDelete(data) {
    return fetch({
        url: 'score/batchDelete',
        method: 'post',
        data: data
    })
}
export function deleteDutyScore(data) {
    return fetch({
        url: 'score/deleteDutyScore',
        method: 'post',
        data: data
    })
}

export function getBadList(data) {
    return fetch({
        url: 'scoreBad/list',
        method: 'post',
        data: data
    })
}

export function getSortList(data) {
    return fetch({
        url: 'scoreSort/list',
        method: 'post',
        data: data
    })
}


export function getScoreDutySmTjList(data) {
    return fetch({
        url: 'scoredutysm/scoreDutySmTjList',
        method: 'post',
        data: data
    })
}

export function getFlowTjList(data) {
    return fetch({
        url: 'scoreflow/scoreFlowTjList',
        method: 'post',
        data: data
    })
}
export function getFlowDetailTjList(data) {
    return fetch({
        url: 'scoreflow/scoreFlowDetailTjList',
        method: 'post',
        data: data
    })
}
export function getUserDzbList(data) {
    return fetch({
        url: 'user/dzblist',
        method: 'post',
        data: data
    })
}