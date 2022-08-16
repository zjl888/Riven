<template>
<div>
  <span>{{user.sex}}</span>
  <span>{{name.lastName}}</span>
</div>
</template>

<script>
export default {
  name: "test1",
  props:{
    user:{
      type:Object
    }
  },
  data(){
    return{
        name:{}
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>