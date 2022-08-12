import axios from "axios";
import {getToken} from "@/utils/cookies";
//import {saveAs} from 'file-saver'
//import errorCode from "@/utils/errorCode";

//设置axios请求头携带的数据格式
axios.defaults.headers['Content-Type']='application/json;charset=utf-8'
//创建axios实例
const service=axios.create({
    //请求URL公共部分
    baseURL:process.env.VUE_APP_BASE_API,
    //超时
    timeout:10000
})

//request拦截器
service.interceptors.request.use(config=>{
    //判断该请求是否需要设置token
    const isToken=(config.headers||{}).isToken===false
    if (getToken()&&!isToken){
        //若需要设置token，将Authorization放于请求头中
        //jwt token的标准写法Authorization: Bearer aaa.bbb.ccc。
        config.headers['Authorization']='Bearer '+getToken()
    }
    return config

},error=>{
    console.log(error)
    Promise.reject(error)
})
//响应拦截器
/*service.interceptors.response.use(res=>{
    //未设置状态码，默认200成功
    //const code=res.data.code||200
    //获取错误信息
    //const msg=errorCode[code]||res.data.msg||errorCode['default']
    //二进制数据则直接返回
/!*    if (res.request.responseType==='blob'||res.request.responseType==='arraybuffer'){
        return res.data
    }*!/
})*/
export default service