import Vue from "vue"
import Router from "vue-router"
Vue.use(Router)

import Layout from "@/layout";
//公共路由
export const constantRoutes=[
    {
      path: '/redirect',
      component:Layout,
      children:[
          {
              path:'/redirect/:path(.*)',
              component:()=>import('@/views/redirect')
          }
      ]
    },
    {
        path: '',
        component: Layout,
        redirect:'firstPage',
        children: [
            {
                path: 'firstPage',
                component:()=>import('@/views/firstPage'),
                name:'firstPage',
                meta:{title:'首页',icon:'dashboard',affix:true}
            }
        ]
    },
    {
    path:'/login',
    component:()=>import('@/views/login')
    },
    {
        path: '/index',
        component:()=>import('@/views/index')
    },
/*    {
        path:'/firstPage',
        component:()=>import('@/views/firstPage')
    },*/
    {
        path: '/register',
        component:()=>import('@/views/register')
    }
]
export default new Router({
    mode:'history',//去除url中的#
    scrollBehavior:()=>({y:0}),
    routes:constantRoutes
})













