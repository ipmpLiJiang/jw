import fetch from '@/utils/fetch'


//查询部门列表
export function treelist(data) {
    return fetch({
        url: 'department/treelist',
        method: 'post',
        data: data
    })
}

//添加部门
export function addDepartment(data) {
    return fetch({
        url: 'department/add',
        method: 'post',
        data: data
    })
}

//修改部门
export function updateDepartment(data) {
    return fetch({
        url: 'department/update',
        method: 'post',
        data: data
    })
}

//删除部门
export function deleteDepartment(data) {
    return fetch({
        url: 'department/delete',
        method: 'post',
        data: data
    })
}