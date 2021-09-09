import fetch from '@/utils/fetch'


//人员管理查询接口
export function list(data) {
    return fetch({
        url: 'medicalEthicsMsgTemp/list',
        method: 'post',
        data:data
    })
}


//书记信息查询接口
export function tree() {
    return fetch({
        url: 'partyBranchRelations/tree',
        method: 'post',
        
    })
}


//书记信息查询列表
export function ethicsUserList(params) {
    return fetch({
        url: 'medicalEthicsUser/list?'+params,
        method: 'get',
        
    })
}


