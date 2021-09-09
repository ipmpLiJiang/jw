import fetch from '@/utils/fetch'


//查询所有短信模板接口
export function getList(data) {
    return fetch({
        url: 'template/list',
        method: 'post',
        data:data
    })
}
//查询所有短信模板接口无分页
export function getAllList(data) {
    return fetch({
        url: 'template/selectTemplateList',
        method: 'post',
        data:data
    })
}
//添加短信模板接口
export function addMessage(data) {
    return fetch({
        url: 'template/add',
        method: 'post',
        data:data
    })
}
//修改短信模板接口
export function updateMessage(data) {
    return fetch({
        url: 'template/update',
        method: 'post',
        data:data
    })
}
//修改短信模板接口
export function deleteMessage(data) {
    return fetch({
        url: 'template/delete',
        method: 'post',
        data:data
    })
}

