<template>
  <div>
    <!--el-container外层容器  container布局容器-->
    <el-container class="home-container">
      <!-- container布局头部 -->
      <el-header class="home-header">
        <span class="home-title">人事管理系统</span>
        <div style="display: flex;align-items: center;margin-right: 7px;">
          <!-- el-badge标记  有未读消息时提示-->
          <el-badge style="margin-right: 1.875rem;" :is-dot="this.$store.state.nfDot"><i class="fa fa-bell-o" @click="goChat" style="cursor: pointer;"></i></el-badge>
          <!-- el-dropdown下拉菜单 -->
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link home-user" style="dispaly: flex;align-items: center;">
              <!-- 设置头像，暂未设置系统默认头像,可以在store那里设置，当login时如果!userfase|userfase=‘’ ，设置默认头像-->
              <i>
                <img v-if="user.userfase != ''" :src="user.userfase" style="width: 2.5rem; margin-right: 0.3125rem; margin-left: 0.3125rem; border-radius: 2.5rem;" />
                {{ user.name }}
              </i>
            </span>
            <!-- slot为dropdown设置下拉菜单不需要点击即可显示下拉菜单 -->
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="user">个人中心</el-dropdown-item>
              <el-dropdown-item>设置</el-dropdown-item>
              <!-- divided分割线，command下拉菜单绑定事件的参数 -->
              <el-dropdown-item command="logout" divided>注销</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <!-- 侧边栏 -->
        <el-aside width="180px" class="home-aside">
          <div style="display: flex;justify-content: flex-start;width: 11.25rem;text-align: left;">
            <!-- 导航栏菜单
            unique-opened 是否只保持一个子菜单的展开,默认为false
            router启用该模式会在激活导航时以 index 作为 path 进行路由跳转-->
            <el-menu style="background: #ECECEC; width: 11.25rem;" router unique-opened>
              <el-submenu index="1">
                <template slot="title">
                  <i class="fa fa-user-circle-o" style="color: #20A0FF;width: 0.875rem;"></i>
                  <span slot="title">员工资料</span>
                </template>
                <el-menu-item index="/emp/basic">基本资料</el-menu-item>
              </el-submenu>
              <!-- <el-submenu index="2">
                <template slot="title">
                  <i class="fa fa-address-card-o" style="color: #20A0FF;width: 0.875rem;"></i>
                  <span slot="title">人事管理</span>
                </template>
                <el-menu-item index="/per/ec">员工奖惩</el-menu-item>
                <el-menu-item index="/per/train">员工培训</el-menu-item>
                <el-menu-item index="/per/salary">员工调薪</el-menu-item>
                <el-menu-item index="/per/mv">员工调动</el-menu-item>
              </el-submenu> -->
              <el-submenu index="3">
                <template slot="title">
                  <i class="fa fa-money" style="color: #20A0FF;width: 0.875rem;"></i>
                  <span slot="title">薪资管理</span>
                </template>
                <el-menu-item index="/sal/sob">工资套账管理</el-menu-item>
                <el-menu-item index="/sal/sobcfg">员工套账设置</el-menu-item>
               <!-- <el-menu-item index="/sal/search">工资表查询</el-menu-item>
                <el-menu-item index="/sal/table">工资表管理</el-menu-item>
                <el-menu-item index="/sal/month">月末处理</el-menu-item> -->
              </el-submenu>
              <!-- <el-submenu index="4">
                <template slot="title">
                  <i class="fa fa-bar-chart" style="color: #20A0FF;width: 0.875rem;"></i>
                  <span slot="title">统计管理</span>
                </template>
                <el-menu-item index="/sta/pers">人事信息统计</el-menu-item>
                <el-menu-item index="/sta/record">人事记录统计</el-menu-item>
                <el-menu-item index="/sta/score">员工积分记录</el-menu-item>
              </el-submenu> -->
              <el-submenu index="5">
                <template slot="title">
                  <i class="fa fa-windows" style="color: #20A0FF;width: 0.875rem;"></i>
                  <span slot="title">系统管理</span>
                </template>
                <el-menu-item index="/sys/basic">基础信息管理</el-menu-item>
                <el-menu-item index="/sys/user">用户管理</el-menu-item>
              </el-submenu>
            </el-menu>
          </div>
        </el-aside>

        <!-- main -->
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item :v-text="this.$route.matched">当前页</el-breadcrumb-item>
          </el-breadcrumb>

          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isDot: false
    };
  },
  methods: {
    // 系统消息
    loadNF() {
      var _this = this;
      this.getRequest('/chat/sysmsgs').then(resp => {
        var isDot = false;
        resp.data.obj.forEach(msg => {
          if (msg.state == 0) {
            isDot = true;
          }
        });
        _this.$store.commit('toggleNFDot', isDot);
      });
    },
    goChat() {
      // console.log('goChat');
      this.$router.push({path:'/chat'});
    },
    handleCommand(cmd) {
      if (cmd == 'logout') {
        // 提示框
        this.$confirm('是否注销?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.getRequest('/logout');
            this.$store.commit('logout');
            this.$router.replace({ path: '/' });
          })
          .catch(() => {
            // $message消息提示
            this.$message({
              type: 'info',
              message: '取消'
            });
          });
      }
      if (cmd == 'user') {
        console.log('个人中心页面');
      }
    }
  },

  //计算属性
  computed: {
    user() {
      return this.$store.state.user;
    }
  }
};
</script>

<style>
.home-container {
  height: 100%;
  position: absolute;
  top: 0px;
  left: 0px;
  width: 100%;
}
.home-header {
  background-color: #20a0ff;
  color: #333;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-sizing: content-box;
  padding: 0px;
}
.home-title {
  color: #fff;
  font-size: 22px;
  display: inline;
  margin-left: 8px;
}
.home-user {
  color: #fff;
  cursor: pointer;
}
.home-userContainer {
  display: inline;
  margin-right: 1.25rem;
}
.home-aside {
  background-color: #ececec;
}
.el-submenu .el-menu-item {
  width: 180px;
  min-width: 175px;
}
.home-main {
  background-color: #fff;
  color: #000;
  text-align: center;
  margin: 0px;
  padding: 0px;
}
</style>
