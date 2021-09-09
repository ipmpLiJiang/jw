import fetch from '@/utils/fetch'
//人员管理查询接口
export function Add(data) {
    return fetch({
        url: 'evaluationDepartment/add',
        method: 'post',
        data: data
    })
}

//党支部树形结构查询接口
export function tree() {
    return fetch({
        url: 'partyBranchRelations/tree',
        method: 'post',

    })
}


//列表信息展示接口
export function getList(data) {
    return fetch({
        url: 'evaluationDepartment/list',
        method: 'post',
        data: data

    })
}
//科室列表详情接口
export function getDeptDetail(id) {
    return fetch({
        url: 'evaluationDepartment/info/' + id,
        method: 'get',
    })
}



//删除信息接口
export function Delete(data) {
    return fetch({
        url: 'evaluationDepartment/delete',
        method: 'post',
        data: data

    })
}

//编辑接口

export function update(data) {
    return fetch({
        url: 'evaluationDepartment/update',
        method: 'post',
        data: data

    })
}

//获取当前登录科室主任下属人员信息

export function listUser(data) {
    return fetch({
        url: 'evaluationDepartment/listUser',
        method: 'post',
        data: data

    })
}


//提交到科室
export function addUse(data) {
    return fetch({
        url: 'evaluationDepartment/addUser',
        method: 'post',
        data: data

    })
}
//科室人员编辑
export function updateBaseInfo(data) {
    return fetch({
        url: 'evaluationDepartment/updateBaseInfo',
        method: 'post',
        data: data

    })
}
//科室列表
export function deptListAll() {
    return fetch({
        url: 'evaluationDepartment/listAll',
        method: 'get',
    })
}
//医德医风批量完成
export function batchFinish(data) {
    return fetch({
        url: 'medicalEvaluation/batchFinish',
        method: 'post',
        data: data

    })
}
//医德医风一键完成
export function finishAll() {
    return fetch({
        url: 'medicalEvaluation/finishAll',
        method: 'post',
    })
}

// 导出数据
export function exportCheckResult(data) {
    return fetch({
        responseType: 'blob',
        // headers: {
        // 'Content-Type': 'application/octet-stream'
        // },
        url: 'calculate/exportCheckResult',
        method: 'post',
        data: data
    })
}


//获取查询信息
export function checkResultDetail(data) {
    return fetch({
        url: 'calculate/checkResultDetail',
        method: 'post',
        data: data
    })
}