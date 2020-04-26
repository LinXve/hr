<template>
  <div>
    <div style="text-align: left;">
      <el-input placeholder="添加职位" size="mini" @keyup.enter.native="addPos" v-model="posName" prefix-icon="el-icon-circle-plus" style="width: 18.75rem;" />
      <el-button @click="addPos" size="mini" type="primary">添加职位</el-button>
    </div>
    <div style="margin-top: 0.625rem;">
      <el-table v-loading="tableLoading" :data="posData" stripe border @selection-change="handleSelectionChange" style="width: 80%;" size="mini">
        <el-table-column type="selection" width="40" ></el-table-column>
        <el-table-column prop="id" label="编号" align="left" width="50" ></el-table-column>
        <el-table-column prop="name" label="职位" align="left" width="100" ></el-table-column>
        <el-table-column prop="enabled" label="启动" align="left"  width="80">
          <template slot-scope="scope">
            {{ scope.row.enabled ? '启用' : '禁用' }}
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" align="left" width="150" >
          <template slot-scope="scope">
            {{ scope.row.createDate | formatDate }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="left" width="250" >
          <template slot-scope="scope">
            <el-button type="warning" size="mini" @click="handleEnabled(scope.$index, scope.row)">{{scope.row.enabled?'禁用':'启动'}}</el-button>
            <el-button type="primary" size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="danger" size="mini" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div style="text-align: left; margin-top: 0.625rem;">
      <el-button type="danger" size="mini" :disabled="multipleSelection.length == 0" @click="deleteMany">批量删除</el-button>
    </div>
    <div style="text-align: left;">
      <el-dialog title="修改职位" :visible.sync="dialogVisible" width="25%">
        <el-input v-model="upDataPos.name" size="mini" placeholder="请输入新的职位名称"></el-input>
        <el-button size="mini" @click="updatePosName">确定</el-button>
        <el-button size="mini" @click="dialogVisible=false">取消</el-button>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      posName: '',//添加时职位名
      upDataPos:{
        name:'',
        id:''
      },//编辑的职位
      tableLoading: false,
      dialogVisible:false,
      posData: [],//表格数据
      multipleSelection: [],
    };
  },
  methods: {
    // 加载allPos
    loadPos() {
      this.tableLoading=true;
      this.getRequest("/sys/basic/allPos").then(resp=>{
        this.tableLoading=false;
        if(resp&&resp.status==200){
          this.posData=resp.data.obj;
        }
      })
    },
    // 添加职位
    addPos: function() {
      this.tableLoading=true;
      this.postRequest("/sys/basic/pos/"+this.posName).then(resp=>{
        this.tableLoading=false;
        if(resp&&resp.status==200){
          this.loadPos();
          this.posName='';
        }
      })
    },

    // 禁用
    handleEnabled(index,row){

      this.tableLoading=true;
      this.putRequest("/sys/basic/disabled/",row).then(resp=>{
          this.tableLoading=false;
          this.loadPos();
      })
    },
    // 编辑框
    handleEdit(index,row){
      this.dialogVisible=true;
      this.upDataPos=row;
    },
    // 删除
    handleDelete(index,row){
        this.$confirm('删除['+row.name+']').then(()=>{
          this.doDelete(row.id);
        }).catch(()=>{
          this.$message({
            type:'info',
            message:'已取消'
          })
        })
    },
    // 全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
       console.log(this.multipleSelection);
    },
    // 批量删除
    deleteMany(){
      this.$confirm("删除"+this.multipleSelection.length+"条数据，是否继续?",'批量删除！').then(()=>{
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
      console.log(ids);
      this.tableLoading=true;
      this.deleteRequest("sys/basic/pos?ids="+ids).then(resp=>{
        this.loadPos();
      })
    },
    // 编辑
    updatePosName(){
      this.tableLoading=true;
      this.putRequest("/sys/basic/pos",this.upDataPos).then(resp=>{
        this.tableLoading=false;
        this.dialogVisible=false;
        if(resp&&resp.status==200){
          return;
        }
        this.loadPos();
      })
    }
  },
  mounted() {
    this.loadPos();
  }
};
</script>

<style></style>
