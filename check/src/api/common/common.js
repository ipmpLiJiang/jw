import fetch from '@/utils/fetch'

//科室列表接口
export function getdeparttree() {
    return fetch({
        url: 'GetDepart/getdeparttree',
        method: 'post',
    })
}

//人员列表接口
export function ShowNo(data) {
    return fetch({
        url: 'ProjectManage/ShowNo1',
        method: 'post',
        data: data
    })
}
//附件在线预览接口
export function preview(data) {
    return fetch({
        url: 'summaryattachment/preview',
        method: 'post',
        data: data
    })
}
//附件下载
export function download(params) {
    return fetch({
        url: 'summaryattachment/download',
        method: 'get',
        params: params
    })
}
//移动端图片上传
export function upload(data) {
    return fetch({
        url: 'mobile/upload',
        method: 'post',
        data: data
    })
}