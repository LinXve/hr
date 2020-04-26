<template>
  <div>
    <el-container>
      <el-header style="padding: 0px;display:flex;justify-content:space-between;align-items: center">
        <!-- 搜索栏 -->
        <div style="display: inline;">
          <el-input
            placeholder="通过员工名搜索员工......"
            @change="keywordsChange"
            style="width: 18.75rem;margin: 0rem;padding: 0rem;"
            size="mini"
            clearable
            @keyup.enter.native="searchEmp"
            :disabled="advanceSearchViewVisible"
            prefix-icon="el-icon-search"
            v-model="keywords"
          ></el-input>
          <el-button type="primary" size="mini" style="margin-left: 0.3125rem;" icon="el-icon-search" @click="searchEmp()">搜索</el-button>
          <el-button slot="refenrce" type="primary" size="mini" style="margin-left: 0.3125rem;" @click="showAdvanceSearchView">
            <i class="fa fa-lg" :class="[advanceSearchViewVisible ? faangledoubleup : faangledoubledown]" style="margin-right: 0.3125rem;"></i>
            高级搜索
          </el-button>
        </div>
        <!-- 导入导出。。。 -->
        <div style="margin-left: 0.3125rem; margin-right: 1.25rem; display: inline;">
          <el-upload
            :show-file-list="false"
            accept="application/vnd.ms-excel"
            action="/employee/basic/importEmp"
            :on-success="fileUploadSuccess"
            :on-error="fileUploadError"
            :disabled="fileUploadBtnText == '正在导入'"
            :before-upload="beforeFileUpload"
            style="display: inline;"
          >
            <el-button size="mini" type="success" :loading="fileUploadBtnText == '正在导入'">
              <i class="fa fa-lg fa-level-up" style="margin-right: 0.3125rem;"></i>
              {{ fileUploadBtnText }}
            </el-button>
          </el-upload>
          <el-button type="success" size="mini" @click="exportEmps">
            <i class="fa fa-lg fa-level-down"></i>
            导出员工表
          </el-button>
          <el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddEmpView">添加员工</el-button>
        </div>
      </el-header>
      <el-main style="padding-left: 0rem;padding-top: 0rem;">
        <div>
          <!-- transition动画标签 -->
          <transition name="slide-fade">
            <div
              style="margin-bottom: 10px;border: 1px;border-radius: 5px;border-style: solid;padding: 5px 0px 5px 0px;box-sizing:border-box;border-color: #20a0ff"
              v-show="advanceSearchViewVisible"
            >
              <el-row>
                <el-col :span="5">
                  政治面貌：
                  <el-select v-model="emp.politicId" style="width: 8.125rem;" size="mini" placeholder="政治面貌">
                    <el-option v-for="item in politics" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  民族：
                  <el-select v-model="emp.nationId" style="width: 8.125rem;" size="mini" placeholder="请选择民族">
                    <el-option v-for="item in nations" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  职位：
                  <el-select v-model="emp.posId" style="width: 8.125rem;" size="mini" placeholder="请选择职位">
                    <el-option v-for="item in positions" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="4">
                  职称：
                  <el-select v-model="emp.jobLevelId" style="width: 8.125rem;" size="mini" placeholder="请选择职称">
                    <el-option v-for="item in joblevels" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-col>
                <el-col :span="7">
                  聘用形式：
                  <el-radio-group v-model="emp.engageForm">
                    <el-radio label="劳动合同">劳动合同</el-radio>
                    <el-radio label="劳务合同">劳务合同</el-radio>
                  </el-radio-group>
                </el-col>
              </el-row>
              <el-row style="margin-top: 0.625rem;">
                <el-col :span="5">
                  所属部门：
                  <el-popover v-model="showOrHidePop2" placement="right" title="请选择部门" trigger="manual">
                    <el-tree :data="deps" :default-expand-all="true" :props="defaultProps" :expand-on-click-node="false" @node-click="handleNodeClick2"></el-tree>
                    <div
                      slot="reference"
                      style="width: 130px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                      @click="showDepTree2"
                      :style="{ color: depTextColor }"
                    >
                      {{ emp.departmentName }}
                    </div>
                  </el-popover>
                </el-col>
                <el-col :span="10">
                  入职日期:
                  <el-date-picker
                    v-model="beginDateScope"
                    unlink-panels
                    size="mini"
                    type="daterange"
                    value-format="yyyy-MM-dd"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                  ></el-date-picker>
                </el-col>
                <el-col :span="5" :offset="4">
                  <el-button size="mini" @click="cancelSearch">取消</el-button>
                  <el-button icon="el-icon-search" type="primary" size="mini" @click="searchEmp">搜索</el-button>
                </el-col>
              </el-row>
            </div>
          </transition>
          <!-- 表格展示data绑定展示的数据，
          tableColumn属性：prop：展示的数据 fixed固定 label标题
          -->
          <el-table :data="emps" v-loading="tableLoading" border stripe @selection-change="handleSelectionChange" size="mini" style="width: 100%;">
            <el-table-column type="selection" align="left" width="30"></el-table-column>
            <el-table-column prop="name" align="left" fixed label="姓名" width="90"></el-table-column>
            <el-table-column prop="workID" width="85" align="left" label="工号"></el-table-column>
            <el-table-column prop="gender" width="85" align="left" label="性别"></el-table-column>
            <el-table-column width="85" align="left" label="出生日期">
              <template slot-scope="scope">
                {{ scope.row.birthday | formatDate }}
              </template>
            </el-table-column>
            <el-table-column prop="idCard" width="150" align="left" label="身份证"></el-table-column>
            <el-table-column prop="wedlock" width="70" label="婚姻状况"></el-table-column>
            <el-table-column prop="nation.name" width="50" label="民族"></el-table-column>
            <el-table-column prop="nativePlace" width="80" label="籍贯"></el-table-column>
            <el-table-column prop="politicsStatus.name" label="政治面貌"></el-table-column>
            <el-table-column prop="email" width="180" align="left" label="电子邮箱"></el-table-column>
            <el-table-column prop="phone" width="100" label="手机号码"></el-table-column>
            <el-table-column prop="address" width="200" align="left" label="联系地址"></el-table-column>
            <el-table-column prop="department.name" align="left" width="100" label="所属部门"></el-table-column>
            <el-table-column prop="position.name" align="left" width="100" label="职位"></el-table-column>
            <el-table-column prop="jobLevel.name" width="100" align="left" label="职称"></el-table-column>
            <el-table-column prop="engageForm" label="聘用形式"></el-table-column>
            <el-table-column width="85" align="left" label="入职日期">
              <template slot-scope="scope">
                {{ scope.row.beginDate | formatDate }}
              </template>
            </el-table-column>
            <el-table-column width="85" align="left" label="转正日期">
              <template slot-scope="scope">
                {{ scope.row.conversionTime | formatDate }}
              </template>
            </el-table-column>
            <el-table-column width="85" align="left" label="合同起始日期">
              <template slot-scope="scope">
                {{ scope.row.beginContract | formatDate }}
              </template>
            </el-table-column>
            <el-table-column width="95" align="left" label="合同截至日期">
              <template slot-scope="scope">
                {{ scope.row.endContract | formatDate }}
              </template>
            </el-table-column>
            <el-table-column align="left" width="70" label="合同期限">
              <template slot-scope="scope">
                {{ scope.row.contractTerm }}年
              </template>
            </el-table-column>
            <el-table-column align="left" prop="tiptopDegree" label="最高学历"></el-table-column>
            <el-table-column fixed="right" label="操作" width="195">
              <template slot-scope="scope">
                <el-button @click="showEditEmpView(scope.row)" style="padding: 0.1875rem 0.25rem 0.1875rem 0.25rem; margin: 0.125rem;" size="mini">编辑</el-button>
                <el-button style="padding: 0.1875rem 0.25rem 0.1875rem 0.25rem; margin: 0.125rem;" size="mini">查看高级资料</el-button>
                <el-button type="danger" style="padding: 0.1875rem 0.25rem 0.1875rem 0.25rem; margin: 0.125rem;" size="mini" @click="deleteEmp(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div style="display: flex;justify-content: space-between;margin: 0.125rem;">
            <el-button type="danger" size="mini" v-if="emps.length > 0" :disabled="multipleSelection.length == 0" @click="deleteManyEmps">批量删除</el-button>
            <!-- 分页 layout属性设置展示区-->
            <el-pagination background :page-size="10" :current-page="currentPage" @current-change="currentChange" layout="total,prev, pager, next" :total="totalCount"></el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
    <el-form :model="emp" :rules="rules" ref="addEmpForm" style="margin: 0rem; padding: 0rem;" validate-on-rule-change>
      <div style="text-align: left;">
        <el-dialog :title="dialogTitle" style="padding: 0rem;" :close-on-click-modal="false" :visible.sync="dialogVisible" width="77%">
          <el-row>
            <el-col :span="6">
              <div>
                <el-form-item label="姓名" prop="name">
                  <el-input prefix-icon="el-icon-edit" v-model="emp.name" size="mini" style="width: 9.375rem;" placeholder="请输入员工姓名"></el-input>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="5">
              <div>
                <el-form-item label="性别" prop="gender">
                  <el-radio-group v-model="emp.gender">
                    <el-radio label="男">男</el-radio>
                    <el-radio style="margin-left: 0.9375rem;" label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="出生日期" prop="birthday">
                  <!-- el-date-picker日期选择器。type设置date或datetime。 -->
                  <el-date-picker
                    v-model="emp.birthday"
                    size="mini"
                    value-format="yyyy-MM-DD HH:mm:ss"
                    style="width: 9.375rem;"
                    type="date"
                    placeholder="出生日期"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="7">
              <div>
                <el-form-item label="政治面貌" prop="politicId">
                  <el-select v-model="emp.politicId" style="width: 12.5rem;" size="mini" placeholder="政治面貌">
                    <el-option v-for="item in politics" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <div>
                <el-form-item label="民族" prop="nationId">
                  <el-select v-model="emp.nationId" style="width: 9.375rem;" size="mini" placeholder="请选择民族">
                    <el-option v-for="item in nations" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="5">
              <div>
                <el-form-item label="籍贯" prop="nativePlace">
                  <el-input v-model="emp.nativePlace" size="mini" style="width: 7.5rem;" placeholder="员工籍贯"></el-input>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="电子邮箱" prop="email">
                  <el-input prefix-icon="el-icon-message" v-model="emp.email" size="mini" style="width: 9.375rem;" placeholder="电子游戏"></el-input>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="7">
              <div>
                <el-form-item label="联系地址" prop="address">
                  <el-input prefix-icon="el-icon-edit" v-model="emp.address" size="mini" style="width: 12.5rem;" placeholder="联系地址"></el-input>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <div>
                <el-form-item label="职位" prop="posId">
                  <el-select v-model="emp.posId" style="width: 9.375rem;" size="mini" placeholder="请选择职位">
                    <el-option v-for="item in positions" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="5">
              <div>
                <el-form-item label="职称:" prop="jobLevelId">
                  <el-select v-model="emp.jobLevelId" style="width: 120px" size="mini" placeholder="请选择职称">
                    <el-option v-for="item in joblevels" :key="item.id" :label="item.name" :value="item.id"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="5">
              <div>
                <el-form-item label="所属部门" prop="departmentId">
                  <!--  el-popover弹窗 trigger触发方式
                   placement出现位置-->
                  <el-popover v-model="showOrHidePop" placement="right" title="请选择部门" trigger="manual">
                    <!--  :default-expand-all是否默认展开所有节点-->
                    <!-- :expand-on-click-node="false"是否点击节点时展开，false时只有点击箭头才会展开 -->
                    <el-tree :data="deps" :default-expand-all="true" :props="defaultProps" :expand-on-click-node="false" @node-click="handleNodeClick"></el-tree>
                    <div
                      slot="reference"
                      style="width: 150px;height: 26px;display: inline-flex;font-size:13px;border: 1px;border-radius: 5px;border-style: solid;padding-left: 13px;box-sizing:border-box;border-color: #dcdfe6;cursor: pointer;align-items: center"
                      @click.left="showDepTree"
                      :style="{ color: depTextColor }"
                    >
                      {{ emp.departmentName }}
                    </div>
                  </el-popover>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="7">
              <div>
                <el-form-item label="电话号码" prop="phone">
                  <el-input prefix-icon="el-icon-phone" v-model="emp.phone" size="mini" style="width: 12.5rem;" placeholder="电话号码"></el-input>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <div>
                <el-form-item label="工号" prop="workID"><el-input v-model="emp.workID" disabled size="mini" style="width: 9.375rem;" placeholder="工号"></el-input></el-form-item>
              </div>
            </el-col>
            <el-col :span="5">
              <div>
                <el-form-item label="学历" prop="tiptpDegree">
                  <el-select v-model="emp.tiptopDegree" style="width: 7.5rem;" size="mini" placeholder="最高学历">
                    <el-option v-for="item in degrees" :key="item.id" :label="item.name" :value="item.name"></el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="毕业院校:" prop="school">
                  <el-input prefix-icon="el-icon-edit" v-model="emp.school" size="mini" style="width: 9.375rem;" placeholder="毕业院校名称"></el-input>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="7">
              <div>
                <el-form-item label="专业名称" prop="specialty">
                  <el-input prefix-icon="el-icon-edit" v-model="emp.specialty" size="mini" placeholder="专业名称" style="width: 12.5rem;"></el-input>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <div>
                <el-form-item label="入职日期" prop="beginDate">
                  <el-date-picker
                    v-model="emp.beginDate"
                    size="mini"
                    style="width: 8.125rem;"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="入职日期"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="转正日期" prop="conversionTime">
                  <el-date-picker
                    v-model="emp.conversionTime"
                    size="mini"
                    style="width: 8.125rem;"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="转正日期"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="合同起始日期" prop="beginContract">
                  <el-date-picker
                    v-model="emp.beginContract"
                    size="mini"
                    style="width: 8.125rem;"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="合同起始日期"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-form-item label="合同截至日期" prop="endContract">
                  <el-date-picker
                    v-model="emp.endContract"
                    size="mini"
                    style="width: 8.125rem;"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="合同截至日期"
                  ></el-date-picker>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <div>
                <el-form-item label="身份证号码" prop="idCard">
                  <el-input prefix-icon="el-icon-edit" v-model="emp.idCard" size="mini" style="width: 11.25rem;" placeholder="员工身份证号码..."></el-input>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
                <el-form-item label="聘用形式" prop="engageForm">
                  <el-radio-group v-model="emp.engageForm">
                    <el-radio label="劳动合同">劳动合同</el-radio>
                    <el-radio label="劳务合同">劳务合同</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
                <el-form-item label="婚姻状况" prop="wedlock">
                  <el-radio-group v-model="emp.wedlock">
                    <el-radio label="未婚">未婚</el-radio>
                    <el-radio style="margin-left: 0.9375rem;" label="已婚">已婚</el-radio>
                    <el-radio style="margin-left: 0.9375rem;" label="离异">离异</el-radio>
                  </el-radio-group>
                </el-form-item>
              </div>
            </el-col>
          </el-row>
          <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="cancelEdit">取消</el-button>
            <el-button size="mini" type="primary" @click="addEmp('addEmpForm')">确定</el-button>
          </span>
        </el-dialog>
      </div>
    </el-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      emps: [],
      keywords: '',
      fileUploadBtnText: '导入数据',
      advanceSearchViewVisible: false, //是否展开高级搜索
      showOrHidePop: false,
      showOrHidePop2: false,
      faangledoubleup: 'fa-angle-double-up',
      faangledoubledown: 'fa-angle-double-down',
      multipleSelection: [], //多选
      // 分页
      currentPage: 1,
      totalCount: -1,

      nations: [], //民族种类
      politics: [], //政治面貌种类
      positions: [], //职位种类
      joblevels: [], //职称种类
      deps: [], //部门
      beginDateScope: '', //入职时间
      depTextColor: '#c0c4cc',
      // Tree Props属性绑定值
      defaultProps: {
        label: 'name',//指定节点标签为节点对象的某个属性值
        isleaf: 'leaf',//
        children: 'childs'
      },
      tableLoading: false,
      dialogTitle: '', //模态框标题
      dialogVisible: false,
      // 员工属性
      emp: {
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: '',
        nativePlace: '',
        politicId: '',
        email: '',
        phone: '',
        address: '',
        departmentId: '',
        departmentName: '所属部门。。。',
        jobLevelId: '',
        posId: '',
        engageForm: '',
        tiptopDegree: '', //最高学历
        specialty: '', //专业
        school: '',
        beginDate: '',
        workState: '',
        workID: '',
        contractTerm: '', //合同期限
        conversionTime: '', //转正日期
        notWorkDate: '',
        beginContract: '', //合同起始日期
        endContract: '', //合同截至日期
        workAge: ''
      },
      // 学历选择...
      degrees: [
        { id: 1, name: '小学' },
        { id: 2, name: '初中' },
        { id: 3, name: '高中' },
        { id: 4, name: '中专' },
        { id: 5, name: '大专' },
        { id: 6, name: '本科' },
        { id: 7, name: '硕士' },
        { id: 8, name: '博士' },
        { id: 9, name: '其他' }
      ],
      rules: {
        name: [{ required: true, message: '姓名非空', trigger: 'blur' }],
        gender: [{ required: true, message: '必填：性别', trigger: 'blur' }],
        birthday: [{ required: true, message: '必填：出生日期', trigger: 'blur' }],
        idCard: [
          {
            required: true,
            message: '必填：身份证',
            trigger: 'blur'
          },
          {
            pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
            message: '身份证号码格式不正确',
            trigger: 'blur'
          }
        ],
        wedlock: [{ required: true, message: '必填婚姻清况', trigger: 'blur' }],
        nationId: [{ required: true, message: '必填:民族', trigger: 'blur' }],
        nativePlace: [{ required: true, message: '必填:籍管', trigger: 'blur' }],
        politicId: [{ required: true, message: '必填:政治面貌', trigger: 'blur' }],
        email: [
          { required: true, message: '必填:电子邮箱', trigger: 'blur' },
          {
            type: 'email',
            message: '邮箱格式不正确',
            trigger: 'blur'
          }
        ],
        phone: [{ required: true, message: '必填:电话号码', trigger: 'blur' }],
        address: [{ required: true, message: '必填:联系地址', trigger: 'blur' }],
        departmentId: [{ required: true, message: '必填:所在部门', trigger: 'change' }],
        jobLevelId: [{ required: true, message: '必填:职称', trigger: 'change' }],
        posId: [{ required: true, message: '必填:职位', trigger: 'change' }],
        engageForm: [{ required: true, message: '必填:聘用形式', trigger: 'blur' }],
        tiptopDegree: [{ required: true, message: '必填:最高学历', trigger: 'blur' }],
        specialty: [{ required: true, message: '必填:专业', trigger: 'blur' }],
        workID: [{ required: true, message: '必填:工号', trigger: 'blur' }],
        school: [{ required: true, message: '必填:毕业院校', trigger: 'blur' }],
        beginDate: [{ required: true, message: '必填:入职日期', trigger: 'blur' }],
        conversionTime: [{ required: true, message: '必填:转正日期', trigger: 'blur' }],
        beginContract: [{ required: true, message: '必填:合同起始日期', trigger: 'blur' }],
        endContract: [{ required: true, message: '必填:合同终止日期', trigger: 'blur' }],
        workAge: [{ required: true, message: '必填:工龄', trigger: 'blur' }]
      }
    };
  },
  mounted: function() {
    this.initData();
    this.loadEmps();
  },
  methods: {
    // 加载员工数据
    loadEmps() {
      this.tableLoading = true;
      this.getRequest('/employee/basic/emp?page=' + this.currentPage
      +'&keywords='+this.keywords+
      '&politicId='+this.emp.politicId+
      '&nationId='+this.emp.nationId+
      '&posId='+this.emp.posId+
      '&jobLevelId='+this.emp.jobLevelId+
      '&departmentId='+this.emp.departmentId+
      '&engageForm='+this.emp.engageForm+
      '&beginDateScope='+this.beginDateScope).then(resp => {
        this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          this.emps = data.emps;
          this.totalCount = data.count;
        }
      });
    },
    // 初始化数据
    initData() {
      this.getRequest('/employee/basic/basicdata').then(resp => {
        if (resp && resp.status == 200) {
          var data = resp.data;
          this.nations = data.nations;

          this.politics = data.politics;

          this.deps = data.deps;

          this.joblevels = data.joblevels;

          this.positions = data.positions;

          this.emp.workID = data.workID;

        }
      });
    },
    // 清空员工数据
    emptyEmpData() {
      this.emp = {
        id:'',
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: '',
        nativePlace: '',
        politicId: '',
        email: '',
        phone: '',
        address: '',
        departmentId: '',
        departmentName: '所属部门。。。',
        jobLevelId: '',
        posId: '',
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '',
        workID: '',
        contractTerm: '',
        conversionTime: '',
        notWorkDate: '',
        beginContract: '',
        endContract: '',
        workAge: ''
      };
    },
    //搜索栏关键词改变
    keywordsChange: function(val) {
      if (val == '') {
        this.loadEmps();
      }
    },
    // 搜索
    searchEmp: function() {
      this.loadEmps();
    },
    // 高级搜索
    showAdvanceSearchView() {
      this.advanceSearchViewVisible = !this.advanceSearchViewVisible;
      this.keywords = '';
      if (!this.advanceSearchViewVisible) {
        // 关闭模态框时清空
        this.emptyEmpData();
        this.beginDateScope = '';
        this.loadEmps();
      }
    },
    // 上传成功时
    fileUploadSuccess(response, file, fileList) {
      if (response) {
        this.$message({ type: response.status, message: response.msg });
      }
      this.loadEmps();
      this.fileUploadBtnText = '导入数据';
    },
    // 上传失败时
    fileUploadError(response, file, fileList) {
      this.$message({ type: 'error', message: '导入失败！' });
      this.fileUploadBtnText = '导入数据';
    },
    beforeFileUpload(file) {
      this.fileUploadBtnText = '正在导入';
    },
    // 导出
    exportEmps() {
      window.open('/employee/basic/exportEmp', '_parent');
    },
    // 打开添加模态框
    showAddEmpView: function() {
      this.dialogTitle = '添加员工';
      this.dialogVisible = true;
      // 获取最大的工号
      this.getRequest('/employee/basic/maxWorkID').then(resp => {
        if (resp && resp.status == 200) {
          this.emp.workID = resp.data;
        }
      });
    },
    // 打开编辑--模态框
    showEditEmpView(row) {
      console.log(row);
      this.dialogTitle = '编辑员工';
      this.emp.id = row.id;
      this.emp.name = row.name;
      this.emp.address = row.address;
      this.emp.gender = row.gender;
      this.emp.email = row.email;
      this.emp.engageForm = row.engageForm;
      this.emp.idCard = row.idCard;
      this.emp.nativePlace = row.nativePlace;
      this.emp.phone = row.phone;
      this.emp.school = row.school;
      this.emp.specialty = row.specialty;
      this.emp.tiptopDegree = row.tiptopDegree;
      this.emp.wedlock = row.wedlock;
      this.emp.workID = row.workID;
      this.emp.workState = row.workState;

      this.emp.birthday = this.formatDate(row.birthday);
      this.emp.conversionTime = this.formatDate(row.conversionTime);
      this.emp.beginContract = this.formatDate(row.beginContract);
      this.emp.endContract = this.formatDate(row.endContract);
      this.emp.beginDate = this.formatDate(row.beginDate);

      this.emp.nationId = row.nation.id;
      this.emp.politicId = row.politicsStatus.id;
      this.emp.departmentId = row.department.id;
      this.emp.departmentName = row.department.name;
      this.emp.jobLevelId = row.jobLevel.id;
      this.emp.posId = row.position.id;
      delete this.emp.workAge;
      delete this.emp.notWorkDate;
      this.dialogVisible = true;
    },
    addEmp(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          if (this.emp.id) {
            //更新
            this.tableLoading = true;
            this.putRequest('/employee/basic/emp', this.emp).then(resp => {
              this.tableLoading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                this.dialogVisible = false;
                this.emptyEmpData();
                this.loadEmps();
              }
            });
          } else {
            // 添加
            this.tableLoading = true;
            this.postRequest('/employee/basic/emp', this.emp).then(resp => {
              this.tableLoading = false;
              if (resp && resp.status == 200) {
                var data = resp.data;
                this.dialogTitle = false;
                this.cancelEdit();
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    handleNodeClick(data) {
      this.emp.departmentName = data.name;
      this.emp.departmentId = data.id;
      this.showOrHidePop = false;
      this.depTextColor = '#606266';
    },
    handleNodeClick2(data) {
      this.emp.departmentId = data.id;
      this.emp.departmentName = data.name;
      this.showOrHidePop2 = false;
      this.depTextColor = '#606266';
    },
    //高级搜索的部门树显示开关
    showDepTree() {
      this.showOrHidePop = !this.showOrHidePop;
    },
    //添加员工功能的部门树显示开关
    showDepTree2() {
      this.showOrHidePop2 = !this.showOrHidePop2;
    },
    // 取消搜索
    cancelSearch() {
      this.advanceSearchViewVisible = false;
      this.emptyEmpData();
      this.beginDateScope = '';
      this.loadEmps();
    },
    // 多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 换页
    currentChange(currentChange) {
      this.currentPage = currentChange;
      this.loadEmps();
    },
    // 关闭模态框
    cancelEdit() {
      this.dialogVisible = false;
      this.emptyEmpData();
      this.loadEmps();
    },
    // 删除操作
    doDelete(ids) {
      this.tableLoading = true;
      this.deleteRequest('/employee/basic/emp/' + ids).then(resp => {
        this.tableLoading = false;
        if (resp && resp.status == 200) {
          var data = resp.data;
          this.loadEmps();
        }
      });
    },
    // 单个删除
    deleteEmp(row) {
      this.$confirm('此操作将删除[' + row.name + '],是否继续?', '提示', {
        confirmButtonText: '删除',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.doDelete(row.id);
      });
    },
    // 批量删除
    deleteManyEmps() {
      this.$confirm('此操作将删除[' + this.multipleSelection.length + ']条数据，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancleButtonText: '取消',
        type: 'warning'
      }).then(() => {
        var ids = '';
        for (var i = 0; i < this.multipleSelection.length; i++) {
          ids += this.multipleSelection[i].id + ',';
        }
        this.doDelete(ids);
      });
    }
  }
};
</script>

<style>
.slide-fade-enter-active {
  transition: all 0.5s ease;
}
.slide-fade-leave-active {
  transition: all 0.5s cubic-bezier(1, 0.5, 0.8, 1);
}
.slide-fade-enter,
.slide-fade-leave-to {
  transform: translateX(0.625rem);
  opacity: 0;
}
</style>
