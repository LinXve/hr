<template>
  <div>
    <el-container>
      <el-header style="display: flex;justify-content: space-between;align-items: center;padding-left: 0rem;">
        <el-button @click="handleAdd" icon="el-icon-plus" type="primary" size="mini">添加账套</el-button>
        <el-button size="mini" type="success" @click="loadSalaryCfg" icon="el-icon-refresh">刷新</el-button>
      </el-header>
      <el-main style="padding-left: 0rem; padding-top: 0rem;">
        <div>
          <!-- stripe设置是否开启斑马纹  border开启竖直方向的边框
          通过 Scoped slot 可以获取到 row, column, $index 和 store
          （table 内部的状态管理）的数据
          -->
          <el-table :data="salaries" stripe border v-loading="tableLoading" size="mini" @selection-change="handleSelectionChange" style="width: 100%;">
            <el-table-column width="30" type="selection"></el-table-column>
            <el-table-column width="120" prop="name" label="名称" align="center"></el-table-column>
            <el-table-column width="70" prop="basicSalary" label="基本工资"></el-table-column>
            <el-table-column width="70" prop="trafficSalary" label="交通补助"></el-table-column>
            <el-table-column width="70" prop="bonus" label="奖金"></el-table-column>
            <el-table-column width="100" label="启用时间">
              <template slot-scope="scope">
                {{ scope.row.createDate | formatDate }}
              </template>
            </el-table-column>
            <el-table-column label="养老金" align="center" style="padding-bottom: 25rem;">
              <el-table-column width="70" prop="pensionPer" label="比率"></el-table-column>
              <el-table-column width="70" prop="pensionBase" label="基数"></el-table-column>
            </el-table-column>
            <el-table-column label="医疗保险" align="center">
              <el-table-column label="比率" prop="medicalPer" width="70"></el-table-column>
              <el-table-column label="基数" prop="medicalBase" width="70"></el-table-column>
            </el-table-column>
            <el-table-column label="公积金" align="center">
              <el-table-column label="比率" prop="accumulationFundPer" width="70"></el-table-column>
              <el-table-column label="基数" prop="accumulationFundBase" width="70"></el-table-column>
            </el-table-column>
            <el-table-column label="所属部门" align="center" prop="department.name">
            </el-table-column>
            <el-table-column label="操作" align="center">
              <el-table-column label="编辑" align="center">
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                </template>
              </el-table-column>
              <el-table-column label="删除" align="center">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table-column>
          </el-table>
        </div>
        <div style="text-align: left;margin-top: 0.625rem;" v-if="salaries != 0">
          <el-button type="danger" round size="mini" :disabled="multipleSelection.length == 0" @click="deleteAll">批量删除</el-button>
        </div>
      </el-main>
    </el-container>
    <div style="text-align: left;">
      <!-- :visible.sync用于控制dialog的开关 -->
      <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%" @close="emptySalary" :close-on-click-modal="false">
        <div style="display: flex; justify-content: flex-start;">
          <!-- 步骤条steps
           :active表明步骤的index，从0开始
           finish-status已完成步骤的状态
            direction="vertical"设置竖直方向的步骤条
           :-->
          <el-steps :active="index" direction="vertical" finish-status="success">
            <!-- title具名分发，slot来取代属性的设置 -->
            <el-step title="基本工资"></el-step>
            <el-step title="交通补助"></el-step>
            <el-step title="午餐补助"></el-step>
            <el-step title="奖金"></el-step>
            <el-step title="养老金"></el-step>
            <el-step title="医疗保险"></el-step>
            <el-step title="公积金"></el-step>
            <el-step title="启用时间"></el-step>
            <el-step title="所属部门"></el-step>
          </el-steps>
          <div style="margin-left: 1.875rem; display: flex; justify-content: center;align-items: center;width: 80%;">
            <div v-show="index == 0">
              基本工资:
              <el-input placeholder="请输入基本工资..." size="mini" style="width: 12.5rem;" type="number" @keyup.enter.native="next" v-model="salary.basicSalary"></el-input>
            </div>
            <div v-show="index == 1">
              交通补助：
              <el-input placeholder="请输入交通补助..." @keyup.enter.native="next" size="mini" style="width: 12.5rem; " type="number" v-model="salary.trafficSalary"></el-input>
            </div>
            <div v-show="index == 2">
              午餐补助:
              <el-input placeholder="请输入午餐补助..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.lunchSalary"></el-input>
            </div>
            <div v-show="index == 3">
              奖金:
              <el-input placeholder="请输入奖金..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.bonus"></el-input>
            </div>
            <div v-show="index == 4">
              <div style="margin-top: 0.625rem;">
                养老金比率:
                <el-input placeholder="养老金比率..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.pensionPer"></el-input>
              </div>
              <div style="margin-top: 0.625rem;">
                养老金基数:
                <el-input placeholder="养老金基数..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.pensionBase"></el-input>
              </div>
            </div>
            <div v-show="index == 5">
              <div style="margin-:top: 0.625rem;">
                医疗保险比率:
                <el-input placeholder="医疗保险比率..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.medicalPer"></el-input>
              </div>
              <div style="margin-top: 0.625rem;">
                医疗保险基数:
                <el-input placeholder="医疗保险基数..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.medicalBase"></el-input>
              </div>
            </div>
            <div v-show="index == 6">
              <div>
                公积金比率:
                <el-input placeholder="公积金比率..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.accumulationFundPer"></el-input>
              </div>
              <div style="margin-top: 0.625rem;">
                公积金基数：
                <el-input placeholder="公积金基数..." @keyup.enter.native="next" size="mini" style="width: 12.5rem;" type="number" v-model="salary.accumulationFundBase"></el-input>
              </div>
            </div>
            <div v-show="index == 7">
              启用时间:
              <el-date-picker v-model="salary.createDate" type="date" size="mini" value-format="yyyy-MM-dd" style="width: 12.5rem;" placeholder="选择日期"></el-date-picker>
            </div>
            <div v-show="index==8">
              所属部门：
                <el-tree :data="deps" :default-expand-all="true" :props="defaultProps" :expand-on-click-node="false" @node-click="handleNodeClick">
                  <div slot="reference"
                      style="width: 130px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center" @click.left="!showDepTree" :style="{ color: depTextColor }">
                      {{salary.department.name}}
                      </div>
                </el-tree>
            </div>
          </div>
        </div>
        <div style="display: flex; justify-content: center; align-items: center; padding: 0rem; margin: 0rem;">
          <el-button round size="mini" v-if="index != 0" @click="index--">上一步</el-button>
          <el-button type="primary" round size="mini" @click="next" v-text="index == 8 ? '完成' : '下一步'"></el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      index: 0,
      dialogTitle: '',
      salaries: [],
      multipleSelection: [],
      showDepTree:false,
      deps: [],
      depTextColor: '#c0c4cc',
      salary: {
        id: '',
        name: '',
        createDate: '',
        basicSalary: '', //基本工资
        trafficSalary: '', //交通补助
        lunchSalary: '', //午餐补助
        bonus: '', //奖金
        pensionPer: '', //养老金比率
        pensionBase: '', //养老金基数
        medicalBase: '', //医疗基数
        medicalPer: '', //医疗比率
        accumulationFundBase: '', //公积金基数
        accumulationFundPer: '' ,//公积金比率\
        departmentId:'',
        department:{}
      },

      // Tree Props属性绑定值
      defaultProps: {
        label: 'name',//指定节点标签为节点对象的某个属性值
        isleaf: 'leaf',//
        children: 'childs'
      },
      dialogVisible: false,
      tableLoading: false
    };
  },
  methods: {
    deleteAll() {
      this.$confirm('删除[' + this.multipleSelection.length + ']条记录，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          var ids = '';
          this.multipleSelection.forEach(row => {
            ids = ids + row.id + ',';
          });
          this.doDelete(ids);
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
    // 全选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 编辑
    handleEdit(index, row) {
      this.dialogVisible = true;
      this.dialogTitle = '编辑工资套账';
      row.createDate = new Date(row.createDate);
      delete row.allSalary;
      this.salary = row;
    },
    handleAdd() {
      this.dialogVisible = true;
      this.dialogTitle = '添加工资套账';
    },
    handleDelete(index, row) {
      this.$confirm('删除[' + row.name + ']账套，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.doDelete(row.id);
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
    },
    doDelete(id) {
      this.tableLoading = true;
      this.deleteRequest('/salary/sob/salary/' + id).then(resp => {
        this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          this.loadSalaryCfg();
        }
      });
    },
    loadSalaryCfg() {
      this.tableLoading = true;
      this.getRequest('/salary/sob/salary').then(resp => {
        this.tableLoading = false;
        if (resp && resp.status == 200) {
          this.salaries = resp.data;

        }
      });
    },
    loadDeps() {
      this.getRequest('/salary/sob/deps').then(resp => {
        if (resp && resp.status == 200) {
          this.deps = resp.data.obj;

        }
      });
    },
    handleNodeClick(data){
      this.salary.departmentId=data.id;
      this.salary.department=data;
      this.depTextColor = '#606266';
    },
    emptySalary() {
      this.salary = {
        id: '',
        createDate: '',
        basicSalary: '',
        trafficSalary: '',
        lunchSalary: '',
        bonus: '',
        pensionBase: '',
        pensionPer: '',
        medicalBase: '',
        medicalPer: '',
        accumulationFundBase: '',
        accumulationFundPer: '',
        departmentId:'',
        department:{}
      };
      this.index = 0;
    },
    next() {
      if (this.index == 8) {
        if (
          this.salary.createDate &&
          this.salary.basicSalary &&
          this.salary.bonus &&
          this.salary.accumulationFundBase &&
          this.salary.accumulationFundPer &&
          this.salary.lunchSalary &&
          this.salary.medicalBase &&
          this.salary.medicalPer &&
          this.salary.pensionBase &&
          this.salary.pensionPer &&
          this.salary.trafficSalary &&
          this.salary.departmentId
        ) {
          console.log(this.salary)
          if (this.salary.id) {
            //更新
            this.tableLoading = true;
            this.putRequest('/salary/sob/salary', this.salary).then(resp => {
              this.tableLoading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                this.dialogVisible = false;
                this.index = 0;
                this.loadSalaryCfg();
              }
            });
          } else {
            // 添加
            this.$prompt('请输入账套名称', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消'
            }).then(({ value }) => {
              this.salary.name = value;
              this.postRequest('/salary/sob/salary', this.salary).then(resp => {
                if (resp && resp.status == 200) {
                  var data = resp.data;
                  this.dialogVisible = false;
                  this.index = 0;
                  this.loadSalaryCfg();
                }
              });
            });
          }
        } else {
          this.$message({ type: 'error', message: '字段不能为空！' });
        }
      } else {
        this.index++;
      }
    }
  },
  mounted: function() {
    this.loadSalaryCfg();
    this.loadDeps();
  }
};
</script>

<style></style>
