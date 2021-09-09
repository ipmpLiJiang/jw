import fetch from '@/utils/fetch'


//查询所有人事评分接口(导入)
export function getList(data) {
    return fetch({
        url: 'personnel/list',
        method: 'post',
        data: data
    })
}


//查询所有人事评分接口（导出）
export function exportGetList(data) {
    return fetch({
        url: 'personnel/selectExportAllLike',
        method: 'post',
        data: data
    })
}
//查询所有人事评分接口（导出）
export function selectDeptCheckByYear(data) {
    return fetch({
        url: 'deptCheck/selectDeptCheckByYear',
        method: 'post',
        data: data
    })
}
//查询部门完成情况接口
export function deptCompleteList(data) {
    return fetch({
        url: 'deptComplete/list',
        method: 'post',
        data: data
    })
}
//确认上传分数pdf接口
export function confirmUpload(data) {
    return fetch({
        url: 'attachment/confirmUpload',
        method: 'post',
        data: data
    })
}
//确认上传分数excel接口
export function confirmImportExcel(data) {
    return fetch({
        url: 'personnel/confirmImportExcel',
        method: 'post',
        data: data
    })
}
//确认上传部门考核办法
export function confirmDept(data) {
    return fetch({
        url: 'deptCheck/confirmUpload',
        method: 'post',
        data: data
    })
}
//部门考核办法列表
export function getDeptList(data) {
    return fetch({
        url: 'deptCheck/getList',
        method: 'post',
        data: data
    })
}
//部门列表
export function getSectionList() {
    return fetch({
        url: 'rater/getDept',
        method: 'post'
    })
}
//非人事部管理员确认上传分数pdf接口
export function confirmUploadByManager(data) {
    return fetch({
        url: 'attachment/confirmUploadByManager',
        method: 'post',
        data:data
    })
}
//非人事部管理员在线预览负责人签字pdf接口
export function getAttachByDepart(data) {
    return fetch({
        url: 'attachment/getAttachByDepart',
        method: 'post',
        data:data
    })
}
//非人事部管理员确认上传部门考核分数pdf接口
export function deptCheckConfirmUploadByManager(data) {
    return fetch({
        url: 'deptCheck/confirmUploadByManager',
        method: 'post',
        data:data
    })
}
//非人事部管理员通过年份查询考核附件信息接口
export function selectDeptCheckByManager(data) {
    return fetch({
        url: 'deptCheck/selectDeptCheckByManager',
        method: 'post',
        data:data
    })
}
//查询所有负责人签字分数pdf接口
export function getAttachmentList(data) {
    return fetch({
        url: 'attachment/getAttachmentList',
        method: 'post',
        data:data
    })
}
//查询分数excel和负责人上传pdf未上传的接口
export function getNoImportExcelAndPdf(data) {
    return fetch({
        url: 'deptComplete/getNoImportExcelAndPdf',
        method: 'post',
        data:data
    })
}
