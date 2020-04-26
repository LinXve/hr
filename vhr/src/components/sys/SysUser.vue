<template>
  <div style="margin-top: 0.625rem;" v-loading="fullLoading">
    <div style="margin: 0.625rem; display: flex;justify-content: center;align-items: center;">
      <el-input
        placeholder="默认展示部分用户,可以通过用户名搜索更多的用户..."
        prefix-icon="el-icon-search"
        size="medium"
        style="width: 25.75rem; margin-right: 0.625rem;"
        v-model="keywords"
        @keyup.enter.native="searchClick"
        clearable
      ></el-input>
      <el-button type="primary" size="medium" icon="el-icon-search" @click="searchClick">搜索</el-button>
    </div>

    <div style="display: flex; justify-content: space-around;flex-wrap: wrap;text-align: left;">
      <el-card style="width: 21.875rem; margin-bottom: 1.25rem;" v-for="(item, index) in users" :key="item.id" v-loading="cardLoading[index]">
        <div slot="header" class="clearfix">
          <template>
            {{ item.name }}
          </template>
          <el-button
            type="text"
            style="color: #f6061b;margin: 0rem; float: right; width: 0.9375rem; height: 0.9375rem;"
            icon="el-icon-delete"
            @click="deleteUser(item)"
          ></el-button>
        </div>
        <div>
          <div style="width: 100%; display: flex; justify-content: center;">
            <img :src="item.userfase" :alt="item.name" style="width: 4.375rem;height: 4.375rem; border-radius: 4.375rem;" />
          </div>
          <div style="margin-top: 1.25rem;">
            <div>
              <span class="user-info">用户名:{{ item.username }}</span>
            </div>
            <div class="user-info" style="display: flex;align-items: center;margin-bottom: 0.1875rem;">
              用户状态:
              <el-switch
                style="display: inline;margin-left: 0.3125rem;"
                v-model="item.state"
                active-color="#13ce66"
                inactive-color="#aaaaaa"
                active-text="启用"
                inactive-text="禁用"
                :key="item.id"
                @change="switchChange(item.state, item.id, index)"
              ></el-switch>
            </div>
            <div class="user-info">
              用户角色:
              <el-tag v-for="role in item.roles" :key="role.rid" type="success" size="mini" style="margin-right: 0.3125rem;" :disable-transitions="false">
                {{ role.description }}
              </el-tag>
              <el-popover v-loading="eploading[index]" placement="right" title="角色列表" width="200" @hide="updateUserRoles(item.id, index)" trigger="click">
                <el-select v-model="selRoles" multiple placeholder="请选择角色">
                  <el-option v-for="ar in allRoles" :key="ar.rid" :label="ar.description" :value="ar.rid"></el-option>
                </el-select>
                <el-button
                  type="text"
                  icon="el-icon-more"
                  style="color: #09c0f6; padding-top: 0rem;"
                  slot="reference"
                  @click="loadSelRoles(item.roles, index)"
                  :disabled="moreBtnState"
                ></el-button>
              </el-popover>
            </div>
            <div>
              <span class="user-info">备注：{{ item.remark }}</span>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fullLoading: false,
      keywords: '',
      cardLoading: [],
      eploading: [],
      users: [],
      selRoles: [], //选定的角色
      allRoles: [], //角色列表
      moreBtnState: false, //禁用开关
      selRolesBak: [] //选中的角色
    };
  },
  methods: {
    initCards() {
      this.fullLoading = true;
      this.getRequest('/sys/user/get?keywords=' + this.keywords).then(resp => {
        if (resp && resp.status == 200) {
          this.users = resp.data.obj;
          var len = resp.data.length;
          this.cardLoading = Array.apply(null, Array(len)).map(function(item, i) {
            return false;
          });
          this.eploading = Array.apply(null, Array(len)).map(function(item, i) {
            return false;
          });
        }
      });
    },
    loadAllRoles() {
      this.getRequest('/sys/basic/roles').then(resp => {
        this.fullLoading = false;
        if (resp && resp.status == 200) {
          this.allRoles = resp.data.obj;
        }
      });
    },
    // 搜索
    searchClick() {
      this.initCards();
      this.loadAllRoles();
    },
    deleteUser(user) {
      this.$confirm('确认删除[' + user.username + ']操作员吗？')
        .then(() => {
          this.deleteRequest('/sys/user/' + user.id).then(resp => {
            this.fullLoading = false;
            if (resp && resp.status == 200) {
              this.initCards();
              this.loadAllRoles();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消对[' + user.username + ']的删除！'
          });
        });
    },
    // 禁用启用
    switchChange(state, id, index) {
      this.cardLoading.splice(index, 1, true);
      this.putRequest('/sys/user/', {
        state: state,
        id: id
      }).then(() => {
        this.cardLoading.splice(index, 1, false);
      });
    },
    updateUserRoles(userId, index) {
      this.moreBtnState = false;
      if (this.selRolesBak.length == this.selRoles.length) {
        for (var i = 0; i < this.selRoles.length; i++) {
          for (var j = 0; j < this.selRolesBak.length; j++) {
            if (this.selRoles[i] == this.selRolesBak[j]) {
              this.selRolesBak.splice(j, 1);
              break;
            }
          }
        }
        
        if (this.selRolesBak.length == 0) {
          return;
        }
      }

      this.putRequest('/sys/user/roles', {
        uid: userId,
        rids: this.selRoles
      }).then(() => {
        this.initCards();
        this.loadAllRoles();
      });
    },
    loadSelRoles(userRoles, index) {
      this.moreBtnState = true;
      this.selRoles = [];
      this.selRolesBak = [];
      userRoles.forEach(role => {
        this.selRoles.push(role.rid);
        this.selRolesBak.push(role.rid);
      });
    }
  },
  mounted: function() {
    this.initCards();
    this.loadAllRoles();
  }
};
</script>

<style></style>
