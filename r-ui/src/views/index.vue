<template>
<div>
  <h1>success</h1>
  <button @click="select">查询</button>
  <button v-hasPermi="['system:user:select']">权限</button>
  <button @click="logicDelete">删除</button>
</div>
</template>

<script>
import {logicDelete, select} from "@/api/test";

export default {
  name: "index",
  data(){
    return{
        username:'zzz'
    }
  },
  methods:{
    select(){
      select(this.username).then(res=>{
        this.$message.success(res.data)
      })
    },
    logicDelete(){
      logicDelete(1006954839845908480).then(res=>{
        console.log(res.data)
      })
    }
  }
}
</script>

<style scoped>

</style>