<template>
  <div>
    <div style="text-align: left;">
      <el-input placeholder="输入部门名称..." style="width: 31.25rem; margin: 0rem;padding: 0rem;" size="medium" prefix-icon="el-icon-search" v-model="keywords"></el-input>
    </div>
    <div>
      <!--filter-node-method 对树节点进行过滤筛选(搜索关键词)时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
        render-content 树节点的内容区的渲染 Function -->
      <el-tree
        :props="defaultProps"
        :data="treeData"
        ref="tree"
        :filter-node-method="filterNode"
        v-loading="treeLoading"
        :default-expand-all="true"
        style="width: 31.25rem; margin-top: 0.625rem;"
        :render-content="renderContent"
      ></el-tree>
      <div style="text-align: left;">
        <el-dialog title="添加子部门" :visible.sync="dialogVisible" width="25%">
          <div>
            <span>上级部门</span>
            <el-select v-model="pDep.name"  style="width: 12.5rem;"  size="medium" disabled>
            </el-select>
          </div>
          <div style="margin-top: 0.625rem;">
            <span>部门名称</span>
            <el-input size="medium" style="width: 12.5rem;" v-model="depName" placeholder="请输入部门名称" @keyup.enter.native="addDep"></el-input>
          </div>
          <span slot="footer" class="dialog-footer">
            <el-button size="medium" @click="dialogVisible = false">取消</el-button>
            <el-button size="medium" type="primary" @click="addDep">添加</el-button>
          </span>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      keywords: '',
      defaultProps: {
        lable: 'name',
        isLeaf: 'leaf',
        children: 'childs'
      },
      treeData: [],
      treeLoading: false,
      dialogVisible: false,
      pDep: '',//添加的上级部门
      depName: ''//添加的部门名
    };
  },
  methods: {
    filterNode(value, data) {
      if (!value) {
        return true;
      }
      return data.name.indexOf(value) !== -1;
    },
    renderContent(h, { node, data, store }) {
      return (
        <span style="flex:1; display: flex; align-items: center; justify-content: space-between;font-size: 14px; padding-right: 8px;">
          <span>
            <span>{ data.name }</span>
          </span>
          <span>
            <el-button style="font-size: 12px;" type="primary" size="mini" style="padding:3px" on-click={() => this.showAddDepView(data, event)}>
              添加子部门
            </el-button>
            <el-button style="font-size: 12px;" type="danger" size="mini" style="padding:3px" on-click={() => this.deleteDep(data, event)}>
              删除部门
            </el-button>
          </span>
        </span>
      );
    },
    showAddDepView(data, event) {
      this.dialogVisible = true;
      this.pDep = data;
      // console.log(data);
      event.stopPropagation(); //阻止事件冒泡
    },
    deleteDep(data, event) {
      if (data.childs.length > 0) {
        this.$message({
          message: '该部门存在子部门，不能删除！',
          type: 'warning'
        });
      } else {
        this.$confirm('删除[' + data.name + ']部门？', '提示', {
          confirmButtonText: '确定',
          concelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            this.treeLoading = true;
            this.deleteRequest('/sys/basic/dep/' + data.id).then(resp => {
              this.treeLoading = false;
              if (resp && resp.status == 200) {
                this.deleteLocalDep(this.treeData, data);
              }
            });
          })
          .catch(() => {
            this.$message({
              type: 'info',
              message: '已取消'
            });
          });
      }
      event.stopPropagation();
    },
    deleteLocalDep(treeData, data) {
      for (var i = 0; i < treeData.length; i++) {
        var td = treeData[i];
        if (td.id == data.id) {
          treeData.splice(i, 1);
          return;
        } else {
          this.deleteLocalDep(td.childs, data);
        }
      }
    },
    addDep() {
      this.dialogVisible = false;
      this.treeLoading = true;
      this.postRequest('/sys/basic/dep', {
        name: this.depName,
        parentId: this.pDep.id
      }).then(resp => {
        this.treeLoading=false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          this.depName = '';
          this.loadTreeData();
        }
      });
    },

    loadTreeData() {
      this.getRequest('sys/basic/dep/-1').then(resp => {
        this.treeLoading = false;
        if (resp && resp.status == 200) {
          this.treeData = resp.data.obj;

        }
      });
    }
  },
  mounted() {
    this.treeLoading = true;
    this.loadTreeData();
  },
  watch: {
    keywords(val) {
      this.$refs.tree.filter(val);
    }
  }
};
</script>

<style></style>
