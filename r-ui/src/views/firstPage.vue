<template>
<div>
  <el-tabs v-model="activeName" type="card">
    <el-tab-pane name="test1">
    <test1 :user="user" ref="test1"></test1>
    </el-tab-pane>
    <el-tab-pane name="test2">
    <test2></test2>
    </el-tab-pane>
  </el-tabs>
</div>
</template>

<script>
import Test1 from "@/views/test1";
import Test2 from "@/views/test2";
export default {
  name: "firstPage",
  components: {Test2, Test1},
  data() {
    return {
      activeName:'test1',
      activeIndex: '1',
      activeIndex2: '1',
      user:{
        name:{
          firstName:'zzz',
          lastName:'kkk'
        },
        sex:'男'
      }
    };
  },
  methods: {
    handleSelect(key, keyPath) {
      console.log(key, keyPath);
    }
  },
  created() {
    this.$refs.test1.name=this.name
  }
}
</script>

<style>
</style>