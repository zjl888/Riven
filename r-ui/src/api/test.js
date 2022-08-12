import request from "@/utils/request";

export function select(data){
    return request({
        url:'/user/select',
        method:'post',
        data:data
    })
}
export function logicDelete(deptId){
    return request({
        url:'/dept/'+deptId,
        method:'delete',
    })
}
export function  selectByDeptId(deptId){
    return request({
        url:'/dept/'+deptId,
        method:'get'
    })
}
export function updateDept(dept){
    return request({
        url:'/dept',
        method:'put',
        data:dept
    })
}