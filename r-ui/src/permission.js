//import NProgress from 'nprogress'
import router from "@/router";
import store from "@/store";
import {getToken} from "@/utils/cookies";
//NProgress.configure({showSpinner: false})

const whiteList=['/login','/register']

router.beforeEach((to,from,next)=>{
    //NProgress.start()
    //判断是否为登录后，cookie中是否有token
    console.log(444)
    if (getToken()){
        console.log(333)
        //登陆后
        if(to.path==='/login'){
            next({path:'/'})
            //NProgress.done()
        }else {
            //判断当前用户是否已经拉取完user—info信息
            store.dispatch('getInfo').then(()=>{
                next()
            }).catch(error=>{
                console.log(error)
                next({path:'/'})
            })
        }
    }else {
        console.log(222)
        //没有token
        if(whiteList.indexOf(to.path)!==-1){
            //在白名单中
            next()
        }else {
            //全部重定向至登录页面
            next('/login')
            //NProgress.done()
        }
    }

})