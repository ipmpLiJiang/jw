import fetch from '@/utils/fetch'

//评分关系管理D
export function getDutyList(data) {
    return fetch({
        url: 'dutyDto/dutylist',
        method: 'post',
        data: data
    })

}
export function getStationDutyList(data) {
    return fetch({
        url: 'dutyDto/dutyStationlist',
        method: 'post',
        data: data
    })

}
