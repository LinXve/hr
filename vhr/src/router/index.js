import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import EmpBasic from '@/components/emp/EmpBasic'
import PerEc from '@/components/personnel/PerEc'
import PerTrain from '@/components/personnel/PerTrain'
import PerSalary from '@/components/personnel/PerSalary'
import PerMv from '@/components/personnel/PerMv'
import SalMonth from '@/components/salary/SalMonth'
import SalSearch from '@/components/salary/SalSearch'
import SalSob from '@/components/salary/SalSob'
import SalSobCfg from '@/components/salary/SalSobCfg'
import SalTable from '@/components/salary/SalTable'
import StaPers from '@/components/statisstics/StaPers'

import StaRecord from '@/components/statisstics/StaRecord'
import StaScore from '@/components/statisstics/StaScore'
import SysBasic from '@/components/sys/SysBasic'
import SysUser from '@/components/sys/SysUser'
import Register from '@/components/register'
import Chat from '@/components/chat/Chat'
Vue.use(Router)

export default new Router({
  routes: [{
      path: '/',
      name: 'Login',
      component: Login,
      hidden: true
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      hidden: true,
      meta:{
         requireAuth: true
      },
      children: [
        {
          path: '/chat',
          name: '消息',
          component: Chat,
          hidden: true,
          meta: {
            keepAlive: false,
            requireAuth: true
          }
        },
        {
          path: '/emp/basic',
          name: '基本资料',
          component: EmpBasic
        },

        {
          path: '/per/ec',
          name: '员工奖惩',
          component: PerEc
        },

        {
          path: '/per/train',
          name: '员工培训',
          component: PerTrain
        },
        {
          path: '/per/salary',
          name: '员工调薪',
          component: PerSalary
        },
        {
          path: '/per/mv',
          name: '员工调动',
          component: PerMv
        },
        {
          path: '/sal/sob',
          name: '工资套账管理',
          component: SalSob
        },
        {
          path: '/sal/sobcfg',
          name: '员工套账设置',
          component: SalSobCfg
        },
        {
          path: '/sal/table',
          name: '工资表管理',
          component: SalTable
        },
        {
          path: '/sal/month',
          name: '月末处理',
          component: SalMonth
        },
        {
          path: '/sal/search',
          name: '工资表查询',
          component: SalSearch
        },
        {
          path: '/sta/pers',
          name: '人事信息统计',
          component: StaPers
        },
        {
          path: '/sta/record',
          name: '人事记录统计',
          component: StaRecord
        },
        {
          path: '/sta/score',
          name: '员工积分记录',
          component: StaScore
        },
        {
          path: '/sys/basic',
          name: '基础设置',
          component: SysBasic
        },
        {
          path: '/sys/user',
          name: '用户设置',
          component: SysUser
        }
      ]
    },
    {
      path: '/register',
      name: '注册',
      component: Register
    }

  ]
})
