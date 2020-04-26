<template>
  <div>
    <el-container>
      <el-header style="display: flex;justify-content: space-between; align-items: center;padding-left: 0rem;">
        <el-button @click="handleAdd" icon="el-icon-plus" type="primary" size="mini">添加</el-button>
        <el-button size="mini" type="success" @click="loadJobLevel" icon="el-icon-refresh"></el-button>
      </el-header>
      <el-main style="padding-left: 0rem; padding: 0.0625rem;">
        <div>
          <el-table :data="jobLevels" stripe border v-loading="tableLoading" size="mini" @selection-change="handleSelectionChange" style="100%">
            <el-table-column type="selection" width="40"></el-table-column>
            <el-table-column prop="id" align="left" width="50" label="编号"></el-table-column>
            <el-table-column prop="name" label="职称名" width="100" align="left"></el-table-column>
            <el-table-column prop="titleLevel" label="职称级别" align="left" width="100"></el-table-column>
            <el-table-column prop="createDate" label="创建时间" align="left" width="180">
              <template slot-scope="scope">
                {{ scope.row.createDate | formatDate }}
              </template>
            </el-table-column>
            <el-table-column prop="enabled" align="left" width="80" label="启用情况">
              <template slot-scope="scope">
                {{ scope.row.enabled ? '启用' : '禁用' }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="300">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                <el-button type="infor" size="mini" @click="handleEnaled(scope.row)">{{scope.row.enabled?'禁用':'启用'}}</el-button>
                <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="text-align: left; margin: 0.0625rem;">
            <el-button type="danger" size="small" :disabled="multipleSelection.length == 0" @click="DeleteMany">批量删除</el-button>
          </div>
        </div>
      </el-main>
    </el-container>
    <!-- dialog -->
    <div style="text-align: left;">
      <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="20rem">
        <div><el-input v-model="jobLevel.name" placeholder="请输入职称名..." size="mini" style="width: 16.25rem; margin: 0.625rem;"></el-input></div>
        <div>
          <el-select v-model="jobLevel.titleLevel" size="mini" style="width: 16.25rem; margin: 0.625rem;">
            <el-option v-for="item in titleLevels" :key="item.id" :value="item.value" :label="item.value"></el-option>
          </el-select>
        </div>
        <div>
          <el-date-picker
            v-model="jobLevel.createDate"
            type="date"
            size="mini"
            value-format="yyyy-MM-dd"
            style="width: 16.25rem; margin: 0.625rem;"
            placeholder="请选择日期"
          ></el-date-picker>
        </div>
        <div style="width: 16.25rem;margin: 0.3125rem;text-align: right;">
          <el-button type="success" size="mini" @click="AddJobLevel">确定</el-button>
          <el-button type="infor" size="mini" @click="handleCancer">取消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      jobLevel: {},
      titleLevels: [
        { id: 1, value: '顶级' },
        { id: 2, value: '正高级' },
        { id: 3, value: '副高级' },
        { id: 4, value: '中级' },
        { id: 5, value: '初级' }],
      tableLoading: false,
      jobLevels: [],
      multipleSelection: [],
      dialogTitle: '',
      dialogVisible: false
    };
  },
  methods: {
    // 加载职位表
    loadJobLevel() {
      this.tableLoading = true;
      this.getRequest('/sys/basic/allJobLevel').then(resp => {
        this.tableLoading = false;
        if (resp && resp.status == 200) {
          this.jobLevels = resp.data.obj;
          // console.log(this.jobLevels);
        }
      });
    },
    // 关闭dialog
    handleCancer(){
      this.dialogVisible=false;
      this.jobLevel={};
    },
    handleEnaled(row) {
      this.tableLoading=true;
      this.putRequest("/sys/basic/JLEnabled",row).then(resp=>{
        this.tableLoading=false;
       if(resp&&resp.status==200){
          this.loadJobLevel();
       }
      })
    },
    handleAdd() {
      this.dialogTitle = '新增职称';
      this.dialogVisible = true;
    },
    handleEdit(row) {
      this.dialogTitle = '编辑';
      this.jobLevel = row;
      this.dialogVisible = true;
      console.log(this.jobLevel);
    },
    // 编辑或者修改
    AddJobLevel() {
      this.tableLoading = true;
      // 新增
      console.log(this.jobLevel);
      if (!this.jobLevel.id) {
        this.postRequest('/sys/basic/addJobLevel', this.jobLevel).then(resp => {
          this.tableLoading = false;
          this.jobLevel = {};
          if (resp && resp.status == 200) {
            this.dialogVisible = false;

            this.loadJobLevel();
          }
        });
      } else {
        // 修改
        this.putRequest('/sys/basic/jobLevel', this.jobLevel).then(resp => {
          this.tableLoading = false;
          if (resp && resp.status == 200) {
            this.dialogVisible = false;
            this.jobLevel = {};
          }

          this.loadJobLevel();
        });
      }
    },
    handleDelete(row) {
      this.$confirm("删除["+row.name+"]?").then(()=>{
        this.doDelete(row.id);
      }).catch(()=>{
        this.$message({
          type:'info',
          message:'已取消'
        })
      })
    },
    DeleteMany() {
      this.$confirm("删除"+this.multipleSelection.length+"条数据，是否继续!").then(()=>{
        var ids='';
        this.multipleSelection.forEach(row=>{
          ids+=row.id+",";
        })
        this.doDelete(ids);
      })
      .catch(()=>{
        this.$message({
          type:'info',
          message:'已取消'
        })
      })
    },
    doDelete(ids){
      this.tableLoading=true;
      this.deleteRequest("/sys/basic/joblevel?ids="+ids).then(resp=>{
        if(resp&&resp.status==200){
          this.loadJobLevel();
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    }
  },
  mounted() {
    this.loadJobLevel();
  }
};
</script>

<style></style>
