import Cookies from 'js-cookie'

/**
 *Token相关操作
 */
const TokenKey='Admin-Token'

export function getToken(){
    return Cookies.get(TokenKey)
}

export function setToken(token){
    console.log('1010')
    return Cookies.set(TokenKey,token)
}

export function removeToken(){
    return Cookies.remove(TokenKey)
}