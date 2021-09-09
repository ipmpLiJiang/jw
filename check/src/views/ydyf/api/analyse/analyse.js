import fetch from '@/utils/fetch'

//文化程度统计接口
export function educationLevel(data) {
    return fetch({
        url: 'calculate/educationLevel',
        method: 'post',
        data:data
        
    })
}


//评优百分比
export function scoreLevel(data) {
    return fetch({
        url: 'calculate/scoreLevel',
        method: 'post',
        data:data
        
    })
}

//政治面貌分析
export function politicalStatus(data) {
    return fetch({
        url: 'calculate/politicalStatus',
        method: 'post',
        data:data
    
    })
}

//统计分析列表
export function listDetail(data) {
    return fetch({
        url: 'partyBranchRelations/listDetail',
        method: 'post',
        data:data
    
    })
}
