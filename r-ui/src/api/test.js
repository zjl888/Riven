import request from "@/utils/request";

export function select(data){
    return request({
        url:'/user/select',
        method:'post',
        data:data
    })
}