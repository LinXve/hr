<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick" style="margin-top: 0.625rem;">
      <el-tab-pane label="系统通知" name="notification">
        <nf></nf>
      </el-tab-pane>
      <el-tab-pane label="好友聊天" name="chat">
        <fc></fc>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
  import FriendChat from '@/components/chat/FriendChat'
  import Notification from '@/components/chat/Notification'
  
  export default{
    data(){
      return{
        activeName:'notification'
      }
    },
    methods:{
      handleClick(){},
    },
    components:{
      'fc':FriendChat,
      'nf':Notification
    }
  }
</script>

<style>
</style>
