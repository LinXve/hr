<template>
  <div>
    <el-container>
      <el-header style="text-align: left;padding-left: 0rem;">
        <el-button type="success" size="mini" plain icon="el-icon-success" @click="allRead">全部标记为已读</el-button>
        <el-button type="primary" size="mini" plain v-if="isAdmin" @click="dialogVisible = true">
          <i class="fa fa-send" style="margin-right: 0.9375rem;"></i>
          发送系统通知
        </el-button>
      </el-header>
      <el-main style="padding: 0rem;">
        <el-collapse accordion style="width: 90%;" @change="handleChange" v-model="mid">
          <el-collapse-item v-for="(msg, index) in sysmsgs" :key="index" :name="msg.msgContent.id">
            <template slot="title">
              <div style="display: flex;justify-content: flex-start;align-content: center;">
                <div style="display: flex;justify-content: center;align-items: center;">
                  <div style="width: 0.5rem;height: 0.5rem; background-color: #ea0206;border-radius: 0.5rem;" v-if="msg.state == 0"></div>
                </div>
                <span style="width: 37.5rem;text-align: left;">{{ msg.msgContent.title }}</span>
                <el-tag>{{ msg.msgContent.createDate | formatDate }}</el-tag>
              </div>
            </template>
            <div class="messageDiv">{{ msg.msgContent.message }}</div>
          </el-collapse-item>
        </el-collapse>
        <div style="color: #ea0206;font-size: 1.125rem;text-align: center;" v-if="sysmsgs.length == 0">暂无通知</div>
      </el-main>
    </el-container>
    <div style="text-align: left;">
      <el-dialog title="发送系统通知" v-loading="dialogLoading" :visible.sync="dialogVisible" width="50%">
        <el-row>
          <el-col :span="3">通知标题:</el-col>
          <el-col :span="21"><el-input v-model="title" size="mini" placeholder="请输入标题"></el-input></el-col>
        </el-row>
        <el-row style="margin-top: 0.625rem;">
          <el-col :span="3">通知内容:</el-col>
          <el-col :span="21"><el-input type="textarea" size="mini" :autosize="{ minRows: 5, maxRows: 10 }" placeholder="请输入通知内容.." v-model="message"></el-input></el-col>
          <el-row style="margin-top: 0.625rem;padding-right: 0.625rem;">
            <el-col :offset="17" >
              <el-button type="infor" size="mini" @click="cancelSend">取消</el-button>
              <el-button type="primary" size="mini" @click="sendNFMsg">
                <i class="fa fa-send" style="margin-right: 0.9375rem;"></i>
                发送
              </el-button>
            </el-col>
          </el-row>
        </el-row>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      dialogLoading: false,
      sysmsgs: [],
      mid: -1,
      title: '',
      message: ''
    };
  },
  mounted() {
    this.initSysMsgs();
  },
  methods: {
    allRead() {
      this.putRequest('/chat/markread', { flag: -1 }).then(resp => {
        if (resp && resp.status == 200) {
          this.$store.commit('toggleNFDot', false);
          this.initSysMsgs();
        }
      });
    },
    initSysMsgs() {
      this.getRequest('/chat/sysmsgs').then(resp => {
        this.sysmsgs = resp.data.obj;
        console.log(this.sysmsgs);
        var isDot = false;
        for(let i=0;i<this.sysmsgs.length;i++){
          if(this.sysmsgs[i].state==0){
            isDot=true;
          }
        }
        this.$store.commit('toggleNFDot', isDot);
      });
    },
    handleChange(newVal) {
      if (newVal == '') {
        return;
      }
      this.putRequest('/chat/markread', { flag: this.mid }).then(resp => {
        if (resp && resp.status == 200) {
          this.initSysMsgs();
        }
      });
    },
    sendNFMsg() {
      this.dialogLoading = true;
      this.postRequest('/chat/nf', { message: this.message, title: this.title }).then(resp => {
        this.dialogLoading = false;
        if (resp && resp.status == 200) {
          this.$store.state.stomp.send('/ws/nf', {}, '');
          this.initSysMsgs();
          this.cancelSend();
        }
      });
    },
    cancelSend() {
      this.dialogVisible = false;
      this.title = '';
      this.message = '';
    }
  },
  computed: {
    isAdmin: function() {
      var roles = this.$store.state.user.roles;
      var isAdmin = false;
      for(let i=0;i<roles.length;i++){
        if(roles[i].role=='admin'){
          isAdmin = true;
        }
      }
      return isAdmin;
    }
  }
};
</script>

<style>
.messageDiv {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  border-style: solid;
  border-width: 0.0625rem;
  border-color: #409eff;
  border-radius: 0.3125rem;
  padding: 0.25rem 0rem 0.25rem 0.5rem;
  box-sizing: border-box;
  height: 100%;
}
</style>
