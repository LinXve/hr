<template>
  <div>
    <div style="text-align: left;" v-loading="loading">
      <el-form :model="newRole" :rules="rules" ref="newRole" inline>
        <el-form-item prop="role" label="role:">
          <el-input v-model="newRole.role" placeholder="请输入角色英文名称..." size="mini" style="width: 15.625rem;"></el-input>
        </el-form-item>
        <el-form-item prop="description" label="description:">
          <el-input v-model="newRole.description" placeholder="请输入角色中文名称..." size="mini" style="width: 15.625rem;"></el-input>
        </el-form-item>
        <el-button type="primary" size="mini" style="margin-left: 0.3125rem;" @click="addNewRole">添加角色</el-button>
      </el-form>
    </div>
    <div style="margin-top: 0.625rem;text-align: left;">
      <!-- 折叠面板 accordion手风琴效果   -->
      <el-collapse v-model="activeColItem" accordion style="width: 31.25rem;" @change="collapseChange">
        <el-collapse-item v-for="item in roles" :title="item.description" :name="item.rid" :key="item.role">
          <el-card class="box-card">
            <div slot="header">
              <el-col :span="20"><span>可访问资源</span></el-col>
              <el-col :span="4"><el-button type="success" size="mini" icon="el-icon-plus" style="float: right;margin:0rem;" @click="handleAddPermission(item)"></el-button></el-col>

            </div>
            <div>
              <el-table :data="item.permissions" border stripe size="mini">
                <el-table-column prop="pid" label="id"></el-table-column>
                <el-table-column prop="name" label="name"></el-table-column>
                <el-table-column prop="permission" label="权限"></el-table-column>
                <el-table-column prop="path" label="访问路径"></el-table-column>
                <el-table-column label="操作" fixed="right">
                  <template slot-scope="scope">
                    <el-button type="warning" size="mini" @click="handleDeletePermission(item.rid, scope.row)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-card>
        </el-collapse-item>
      </el-collapse>
    </div>
    <div>
      <el-dialog :visible.sync="dialogVisiable" title="添加权限">
        <el-select multiple v-model="updatePid"><el-option v-for="per in allPermission" :key="per.pid" :value="per.pid" :label="per.name"></el-option></el-select>
        <el-button type="primary" size="mini" @click="addPermission">确定</el-button>
        <el-button type="infor" size="mini" @click="cancelDialog">取消</el-button>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeColItem: -1,
      roles: [],
      allPermission: [],
      updateRid: '',
      updatePid: [],
      newRole: {
        role: '',
        description: ''
      },
      rules: {
        role: [
          {
            required: true,
            message: '角色名(English)不能为空!',
            trigger: 'blur'
          }
        ],
        description: [
          {
            required: true,
            message: '角色名(中文)不能为空!',
            trigger: 'blur'
          }
        ]
      },
      loading: false,
      dialogVisiable: false
    };
  },
  methods: {
    addNewRole() {
      this.$refs.newRole.validate(valid => {
        if (valid) {
          this.loading = true;
          this.postRequest('/sys/basic/addRole', {
            role: this.newRole.role,
            description: this.newRole.description
          }).then(resp => {
            if (resp && resp.status == 200) {
              this.initRoles();
              this.newRole = {
                role: '',
                description: ''
              };
            }
          });
        }
      });
    },
    handleAddPermission(role) {
      this.initPermissions(role.rid);
      this.updateRid = role.rid;
      this.updatePid.pid = role.permissions.pid;
      this.dialogVisiable = true;
    },
    addPermission() {
      this.loading=true;
      this.postRequest("/sys/basic/addPermissions",{
        pids:this.updatePid,
        rid:this.updateRid
      }).then(resp=>{
        if(resp&&resp.status==200){
          this.updatePid=[];
          this.updateRid='';
          this.dialogVisiable=false;
        }
          this.initRoles();
      })

    },
    cancelDialog() {
      this.updatePid = [];
      this.updateRid='';
      this.allPermission = [];
      this.dialogVisiable = false;
    },
    collapseChange() {
      this.activeColItem = -1;
    },
    initRoles() {
      this.getRequest('/sys/basic/roles').then(resp => {
        this.loading = false;
        if (resp && resp.status == 200) {
          this.roles = resp.data.obj;
          this.activeColItem = -1;
        }
      });
    },
    // 获取所有该角色没有的权限
    initPermissions(rid) {
      this.allPermission=[];
      this.getRequest('/sys/basic/permissionTree?rid=' + rid).then(resp => {
        if (resp && resp.status == 200) {
          this.allPermission = resp.data.obj;
        }
      });
    },
    handleDeletePermission(rid, permission) {
      console.log("rid="+rid+",pid="+permission.pid);
      this.$confirm('确定删除[' + permission.name + ']权限吗?')
        .then(() => {
          this.loading = true;
          this.deleteRequest('/sys/basic/deleteRolePermission?rid=' + rid + '&pid=' + permission.pid).then(resp => {
            if (resp && resp.status == 200) {
              this.initRoles();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    }
  },
  mounted() {
    this.loading = true;
    this.initRoles();
  }
};
</script>

<style></style>
