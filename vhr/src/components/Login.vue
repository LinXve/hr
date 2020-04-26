<template>
  <div>
    <el-form :rules="rules" :model="loginForm" class="login-container" label-position="left" ref="loginForm" label-width="80px" >
      <h3 class="login_title">人事管理系统登录</h3>
      <el-form-item prop="username" label="登录名:">
        <el-input type="text" v-model="loginForm.username"
         auto-complete="on" />
      </el-form-item>
      <el-form-item prop="password" label="密 码:">
        <el-input type="password" v-model="loginForm.password" auto-complete="on" @keyup.enter.native="submitClick" ></el-input>
      </el-form-item>
      <el-checkbox class="login_remember" v-model="checked"  label-position="left">记住密码</el-checkbox>
      <el-form-item style="width: 100%;">
        <el-button class="el-button el-button--primary is-plain" style="width: 40%;" v-loading="loading" @click="submitClick">登录</el-button>
         <el-button class="el-button el-button--info is-plain" style="width: 40%;" v-loading="loading" @click="gotoReg">注册</el-button>
      </el-form-item>
    </el-form>
  </div>


</template>
<!-- v-loading的作用动画加载 -->
<script>
  export default{
    data(){
      return{
        rules: {
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码' , trigger: 'blur'}]
        },
        checked: true,
        loginForm: {
          username: 'admin',
          password: '123'
        },
        loading: false
      }
    },
    methods: {
      submitClick: function(){

        var _this=this;
        _this.$refs.loginForm.validate(valid =>{
          if(valid) {
          this.loading = true;
          this.postRequest('/login',{
            username: this.loginForm.username,
            password: this.loginForm.password
          }).then(resp=>{
              // then方法在在postRequest方法执行成功之后执行
            _this.loading= false;
            if(resp && resp.status == 200){

               var data = resp.data;
              _this.$store.commit('login',data.obj);

              var path=_this.$route.query.redirect;
              // 路由跳转
              _this.$router
                .replace({path: path == '/' || path == undefined ? '/home' : path});
            }
          })
          }else {
            return false;
          }
        })
      },
      gotoReg:function(){
        this.$router.push({path:'/register'});
      }
    }
  }
</script>

<style>
 .login-container {
   border-radius: 15px;
   background-clip: padding-box;
   margin: 180px auto;
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
 .login_remember {
   margin: 0px 0px 35px 0px;
   text-align: left;
 }
</style>
