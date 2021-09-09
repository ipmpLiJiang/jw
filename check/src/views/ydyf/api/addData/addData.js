import fetch from '@/utils/fetch'



//人员管理查询接口
export function list(data) {
    return fetch({
        url: 'medicalEthicsMsgTemp/list',
        method: 'post',
        data:data
    })
}
//新增人员管理接口
export function add(data) {
    return fetch({
        url: 'medicalEthicsMsgTemp/add',
        method: 'post',
        data:data
    })
}

//删除人员管理接口
export function Delete(data){
    return fetch({
        url: 'medicalEthicsMsgTemp/delete',
        method: 'post',
        data:data
    })
}

//更新人员管理接口
export function update(data){
    return fetch({
        url: 'medicalEthicsMsgTemp/update',
        method: 'post',
        data:data
    })
}

//人员管理模板下载
export function downloadExcel(){
    return fetch({
        url: 'medicalEthicsMsgTemp/downloadExcel',
        method: 'post',
    })
}

//人员管理模板上传
export function readExcel(data){
    return fetch({
        url: 'medicalEthicsMsgTemp/readExcel',
        method: 'post',
        data: data
    })
}


//已提交数据查询接口
export function realList(){
    return fetch({
        url: 'medicalEthicsMsg/list',
        method: 'post',
    })
}

//已确定提交数据
export function doSubmit(data){
    return fetch({
        url: 'medicalEthicsMsg/doSubmit',
        method: 'post',
        data: data
    })
}


//登录获取权限接口
export function authority(){
    return fetch({
        url: 'medicalEthicsUser/showSingleRole',
        method: 'post',
        
    
    })
}

//已提交人员信息
export function Submit(data){
    return fetch({
        url: 'medicalEthicsMsg/list',
        method: 'post',
        data:data
        
    
    })
}


//已提交用户编辑
export function updateMsg(data){
    return fetch({
        url: 'medicalEthicsMsgTemp/updateMsg',
        method: 'post',
        data:data
    })
}

//我的考评列表接口
export function getMyList(params) {
    return fetch({
        url: 'medicalEthicsMsg/getMyList',
        method: 'get',
        params:params
    })
}

//管理员删除权限接口
export function AdministratorDelete(data){
    return fetch({
        url: 'medicalEthicsMsg/delete?userId='+data,
        method: 'get',
    })
}

//统计分析报表接口
export function checkResult(data){
    return fetch({
        url: 'calculate/checkResult',
        method: 'post',
        data:data
    })
}


//人员权限管理查询接口
export function examineList(data){
    return fetch({
        url: 'medicalPerm/list',
        method: 'post',
        data:data
    })
}

//监听键盘回显
export function searchUser(data){
    return fetch({
        url: 'medicalPerm/searchUser',
        method: 'post',
        data:data
    })
}



//提交权限人员
export function insert(data){
    return fetch({
        url: 'medicalPerm/insert',
        method: 'post',
        data:data
    })
}

//停用开启接口
export function jurisdictionUpdate(data){
    return fetch({
        url: 'medicalPerm/update',
        method: 'post',
        data:data
    })
}

//删除权限接口
export function jurisdictionDelete(data){
    return fetch({
        url: 'medicalPerm/delete',
        method: 'post',
        data:data
    })
}
