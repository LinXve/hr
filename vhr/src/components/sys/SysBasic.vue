<template>
  <div style="margin-top: 0.625rem;">
    <el-tabs v-model="defaultTab" type="card">
      <el-tab-pane label="部门管理" name="depMana">
        <dep-mana></dep-mana>
      </el-tab-pane>
      <el-tab-pane label="职位管理" name="positionMana">
        <pos-mana state="position"></pos-mana>
      </el-tab-pane>
      <el-tab-pane label="职称管理" name="jobTitleMana">
        <jobtitle-mana state="jobtitle"></jobtitle-mana>
      </el-tab-pane>
     <!-- <el-tab-pane label="奖惩规则" name="ecCfg">
        <ec-mana></ec-mana>
      </el-tab-pane> -->
      <el-tab-pane label="权限组" name="menuRole">
        <menu-role></menu-role>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
 import DepMana from './basic/DepMana'
 import PosMana from './basic/PosMana'
 import JobTitleMana from './basic/JobTitleMana'
 import EcMana from './basic/EcMana'
 import MenuRole from './basic/MenuRole'
 export default{
   data(){
     return{
       defaultTab:'depMana'
     };
   },
   components:{
     'dep-mana':DepMana,
     'pos-mana':PosMana,
     'jobtitle-mana':JobTitleMana,
     'ec-mana':EcMana,
     'menu-role':MenuRole
   }
 }
</script>

<style>
</style>
