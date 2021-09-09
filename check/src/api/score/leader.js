import fetch from '@/utils/fetch'


//打分用户列表
export function getList(data) {
    return fetch({
        url: 'history/gradeList',
        method: 'post',
        data: data
    })
}
