import request from "@/utils/request";

//登录方法
export function login(username,password){
    const data={
        username,
        password
    }
    return request({
        url:'/login',
        headers:{
            //是否需要携带token
            isToken:false
        },
        method:'post',
        data:data
    })
}
//注册
export function register(data){
    return request({
        url:'/register',
        headers:{
            isToken: false
        },
        method:'post',
        data:data
    })
}
//退出
export function logOut(){
    return request({
        url:'/logOut',
        method:'post'
    })
}
//获取用户信息
export function getInfo(){
    return request({
        url:'/user/getInfo',
        method:'get'
    })
}