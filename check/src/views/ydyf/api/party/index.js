import fetch from '@/utils/fetch'



//党支部列表
export function branchTree() {
    return fetch({
        url: 'partyBranchRelations/branchTree/',
        method: 'get',
       
    })
}

//党组织管理 查询党组织下级
export function list(data) {
    return fetch({
        url: 'partyBranchRelations/listPage',
        method: 'post',
        data:data
      
       
    })
}


//党组织管理 删除党组织
export function Delete(data) {
    return fetch({
        url: 'partyBranchRelations/delete/'+data,
        method: 'post',
       
    })
}
//党组织管理 更新编辑党组织
export function update(data) {
    return fetch({
        url: 'partyBranchRelations/update',
        method: 'post',
        data:data
       
    })
}

//党组织管理 新增党组织
export function Add(data) {
    return fetch({
        url: 'partyBranchRelations/insert',
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




//新增党支部 获取党委信息

export function headquartersList(data) {
    return fetch({
        url: 'partyBranchRelations/list?level='+data,
        method: 'get',
       
        
    })
}

//新增党总支部 

export function AddGeneral(data) {
    return fetch({
        url: 'partyBranchRelations/insert',
        method: 'post',
        data:data
        
    })
}

//获取党委、党总支列表树
export function partyList() {
    return fetch({
        url: 'partyBranchRelations/treeParent',
        method: 'get',
        
        
    })
}

//搜索
export function search(data) {
    return fetch({
        url: 'partyBranchRelations/branchTree?relationsName='+data,
        method: 'get',
        
        
    })
}



