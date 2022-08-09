import Vue from "vue"
import Vuex from 'vuex'
import getters from "@/store/getters";
import loginOut from "@/store/modules/loginOut";

Vue.use(Vuex)

const store=new Vuex.Store({
    modules:{
        loginOut
    },
    getters,
});
export default store