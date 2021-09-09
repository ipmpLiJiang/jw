import fetch from '@/utils/fetch'


//查询月份总结接口
export function getList(data) {
    return fetch({
        url: 'history/list',
        method: 'post',
        data: data
    })
}

//一键导出未评分和未完成用户
export function oneClickDown(data) {
    return fetch({
        url: 'history/oneClickDown',
        method: 'post',
        data: data
    })
}

