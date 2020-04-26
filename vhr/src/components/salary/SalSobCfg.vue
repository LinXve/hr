<template>
  <div style="margin-top: 0.625rem;">
    <div>
      <el-input style="margin: initial;" size="mini" prefix-icon="el-icon-search"
      clearable @keyup.enter.native="loadEmps" v-model="keywords"
      placeholder="按员工姓名关键词搜索..."
      ></el-input>
    </div>
    <el-table :data="emps" size="mini" border stripe v-loading="tableLoading" style="width: 100%;">
      <el-table-column prop="id"  align="center" fixed="left" label="员工编号"></el-table-column>
      <el-table-column prop="name" align="left" fixed="left" label="姓名" ></el-table-column>
      <el-table-column prop="workID"  align="left" label="工号"></el-table-column>
      <el-table-column prop="email"  align="left" label="电子邮件"></el-table-column>
      <el-table-column prop="phone"  align="left" label="电话"></el-table-column>
      <el-table-column prop="department.name" align="left" label="所属部门"></el-table-column>
      <el-table-column label="工资套账" align="center">
        <template slot-scope="scope">
          <el-tooltip placement="right">
            <div slot="content">
              <template v-if="scope.row.department.salary">
                <div>
                  <el-tag size="mini">基本工资</el-tag>
                  ￥{{ scope.row.department.salary.basicSalary }}
                </div>
                <div>
                  <el-tag size="mini">奖金</el-tag>
                  ￥{{ scope.row.department.salary.bonus }}
                </div>
                <div>
                  <el-tag size="mini">午餐补助</el-tag>
                  ￥{{ scope.row.department.salary.lunchSalary }}
                </div>
                <div>
                  <el-tag size="mini">交通补助</el-tag>
                  ￥{{scope.row.department.salary.trafficSalary}}
                </div>
                <div>
                  <el-tag size="mini">养老金比率</el-tag>
                  ￥{{scope.row.department.salary.pensionPer}}
                </div>
                <div>
                  <el-tag size="mini">养老金基数</el-tag>
                  ￥{{scope.row.department.salary.pensionBase}}
                </div>
                <div>
                  <el-tag size="mini">公积金比率</el-tag>
                  ￥{{scope.row.department.salary.accumulationFundPer}}
                </div>
                <div>
                  <el-tag size="mini">公积金基数</el-tag>
                  ￥{{scope.row.department.salary.accumulationFundBase}}
                </div>
                <div>
                  <el-tag size="mini">医疗保险比率</el-tag>
                  ￥{{scope.row.department.salary.medicalPer}}
                </div>
                <div>
                  <el-tag size="mini">医疗保险基数</el-tag>
                  ￥{{scope.row.department.salary.medicalBase}}
                </div>

              </template>
            </div>
            <el-tag size="mini">{{scope.row.department.salary?scope.row.department.salary.name:'暂未设置'}}</el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
    <!--  <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-popover placement="right" width="200" @hide="updateSalaryCfg(scope.row.id)" :key="scope.row.id" trigger="click">
            <el-select size="mini" v-model="sid" placeholder="请选择">
              <el-option v-for="item in salaries" :key="item.id" :label="item.name" :value="item.id"></el-option>
            </el-select>
            <el-button size="mini" slot="reference" type="danger" @click="showUpdateView(scope.row)">设置账套</el-button>
          </el-popover>
        </template>
      </el-table-column> -->
    </el-table>
    <!-- 分页 -->
    <div style="text-align: right; margin-top: 0.625rem;" >
      <el-pagination background @current-change="currentChange" layout="prev,pager,next" :total="totalCount"></el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      emps: [],
      keywords:'',
      salaries: [],
      tableLoading: false,
      totalCount: -1,
      sid: '',
      osid: '',
      currentPage: 1
    };
  },
  methods: {
    // // 加载薪资套账表
    // loadSalaries() {
    //   this.getRequest("/salary/sobcfg/salaries").then(resp=>{
    //     if(resp&& resp.status==200){
    //       this.salaries=resp.data;
    //     }
    //   })
    // },
    // 员工表
    loadEmps() {

      this.tableLoading=true;
      this.getRequest("/salary/sobcfg/emp?page="+this.currentPage+"&keywords="+this.keywords).then(resp=>{
        this.tableLoading=false;
        if(resp && resp.status==200){
          var data=resp.data;
          this.emps=data.emps;
          this.totalCount=data.count;
        }
      })
    },

    currentChange(currentPage){
      this.currentPage=currentPage;
      this.loadEmps();
    }
  },
  mounted() {
    this.loadEmps();
  }
};
</script>

<style></style>
