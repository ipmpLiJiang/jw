import fetch from '@/utils/fetch'

//评分关系管理D
export function getDutyList(data) {
    return fetch({
        url: 'dutyDto/dutylist',
        method: 'post',
        data: data
    })

}