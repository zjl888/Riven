<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginBody"  class="login-form">
      <h3 class="title">后台管理系统</h3>
      <el-form-item prop="username">
        <el-input
            v-model="loginBody.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="loginBody.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="login"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
<!--      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click.native.prevent="login"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright ©2022 Riven All Rights Reserved.</span>
    </div>
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
        this.$router.push("/index")
      }).catch(()=>{
        console.log(555)
        this.loading=false
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/loginBackGroudIMG/login.jpg");
  background-size: cover;
}
.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
.el-input {
  height: 38px;
input {
  height: 38px;
}
}
.input-icon {
  height: 39px;
  width: 14px;
  margin-left: 2px;
}
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 38px;
  float: right;
img {
  cursor: pointer;
  vertical-align: middle;
}
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>