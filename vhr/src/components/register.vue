<template>
  <div>
    <el-form :rules="rules" :model="user" class="login-container" label-position="left" ref="loginForm" label-width="80px" >
      <h3 class="login_title">注册页面</h3>
      <el-form-item prop="username" label="用户名">
        <el-input type="text" v-model="user.username"
         auto-complete="on" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item prop="password" label="密码">
        <el-input type="password" v-model="user.password" placeholder="请输入密码"
         auto-complete="on" @keyup.enter.native="register" ></el-input>
      </el-form-item>
      <el-form-item style="width: 100%;">
        <el-button class="el-button el-button--primary is-plain" style="width: 40%;" v-loading="loading" @click="register">注册</el-button>
        <el-button class="el-button el-button--info is-plain" style="width: 40%;" v-loading="loading" @click="gotoLogin">返回登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    data(){
      return {
        rules: {
          username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码' , trigger: 'blur'}]
        },
        user: {
          username:'',
          password:''
        },
        loading:false
      }
    },
    methods:{
      register:function(){
        this.$refs.loginForm.validate(valid =>{
          if (valid) {
            this.loading=true;
            this.postRequest('/sys/user/UserReg',{
              username: this.user.username,
              password: this.user.password
            }).then(resp =>  {
              this.loading=false;
              if (resp && resp.status==200) {
                // 注册成功并返回登录
                this.$router.push({path:'/'});
              }
            })
          }
         })
      },
      gotoLogin:function(){
        this.$router.push({path:'/'});
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
</style>
