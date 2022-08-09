<template>
<div>
  <body id="poster">
  <el-form class="login-container" label-position="left" label-width="0px">
    <h3 class="login_title">系统登录</h3>
    <el-form-item>
      <el-input type="text" v-model="loginBody.username" auto-complete="off" placeholder="账号"></el-input>
    </el-form-item>

    <el-form-item>
      <el-input type="password" v-model="loginBody.password" auto-complete="off" placeholder="密码"></el-input>
    </el-form-item>

    <el-form-item style="width: 100%">
      <el-button type="primary" style="width: 100%;background: #505458;border: none" v-on:click="login" :loading="loading">
        <span v-if="!loading">登录</span>
        <span v-else>登录中...</span>
      </el-button>
    </el-form-item>
  </el-form>
  </body>
</div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name:'login',
  data(){
    return{
      loginBody: {
        username: '',
        password: ''
      },
      loading:false
    }
  },
  methods:{
    login(){
      console.log(11)
      this.$store.dispatch("Login",this.loginBody).then(()=>{
        console.log(666)
        this.$router.push("/firstPage")
      }).catch(()=>{
        console.log(555)
        this.loading=false
      })

    }
  }
}
</script>
<style>
#poster {
  background:url("../assets/loginBackGroudIMG/login.jpg") no-repeat;
  background-position: center;
  height: 100%;
  width: 100%;
  background-size: cover;
  position: fixed;
}
body{
  margin: 0px;
  padding: 0;
}

.login-container {
  border-radius: 15px;
  background-clip: padding-box;
  margin: 90px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;
}

.login_title {
  margin: 0px auto 40px auto;
  text-align: center;
  color: #505458;
}
</style>