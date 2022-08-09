import {getToken, setToken} from "@/utils/cookies";
import {getInfo, login} from "@/api/login";

const loginOut={
    state:{
        token:getToken(),
        permissions:[],
    },
    mutations:{
        SET_TOKEN:(state,token)=>{
            state.token=token
        },
        SET_PERMISSIONS:(state,permissions)=>{
            state.permissions=permissions
        }
    },
    actions:{
        //登录
        Login({commit},LoginBody){
            //trim去除字符串头尾的空格
            const username=LoginBody.username.trim()
            const password=LoginBody.password
            return new Promise((resolve,reject)=>{
                login(username,password).then(res=>{
                    console.log(777)
                    console.log(res)
                    console.log(res.data)
                    console.log(res.data.token)
                    setToken(res.data.token)
                    console.log(888)
                    commit("SET_TOKEN",res.data.token)
                    console.log(999)
                    resolve()

                }).catch(err=>{
                    reject(err)
                })
            })
        },

        //获取登录用户信息
        getInfo({commit}){
            return new Promise((resolve,reject)=>{
                getInfo().then(res=>{
                    const permissions=res.data.permissions
                    commit('SET_PERMISSIONS',permissions)
                    resolve(res)
                }).catch(error=>{
                    reject(error)
                })
            })
        }

    }
}
export default loginOut