<template>
  <div>
    <el-container>
      <el-aside width="160px">
        <div class="friendListDiv" v-for="(friend,index) in friends" :key="friend.id" @click="toggleFriend(friend)" :class="{ currentChatFriend: currentFriend.id == friend.id }">
          <img :src="friend.userfase" class="userfaseImg" />
          <el-badge :is-dot="isDotMap.get('isDot#' + currentUser.username + '#' + friend.username) != null">{{ friend.name }}</el-badge>
        </div>
        <div style="background-color: #20A0FF;height: 0.0625rem;width: 10rem;"></div>
      </el-aside>
      <el-main style="padding-top: 0rem;padding-bottom: 0rem;">
        <div class="chatDiv" ref="chatDiv" id="chatDiv">
          <p v-show="currentFriend.name">
            与
            <el-tag type="primary" size="small" style="margin-left: 0.3125rem;margin-right: 0.3125rem;">{{ currentFriend.username }}</el-tag>
            聊天中
          </p>
          <template v-for="msg in msgList">
            <!-- !!!接受其他人发来的消息 -->
            <div style="display: flex;justify-content: flex-start;align-items: center;box-sizing: border-box;" v-if="msg.from == currentFriend.username">
              <img :src="currentFriend.userfase" class="userfaseImg" />
              <div
                style="display: inline-flex;border-style: solid;border-width: 0.0625rem;border-color: #20A0FF;border-radius: 0.3125rem;padding: 0.3125rem 0.5rem 0.3125rem 0.5rem;"
              >
                {{ msg.msg }}
              </div>
            </div>
            <!-- 发送消息 -->
            <div v-if="msg.from != currentFriend.username" style="display: flex;justify-content: flex-end; align-items: center; box-sizing: border-box;">
              <div
                style="display: inline-flex;border-style: solid;border-width: 1px;border-color: #20a0ff;border-radius: 5px;padding: 5px 8px 5px 8px;margin-right: 3px;background-color: #9eea6a;"
              >
                {{ msg.msg }}
              </div>
              <img :src="currentUser.userfase" class="userfaseImg" />
            </div>
          </template>
        </div>
        <div style="text-align: left;margin-top: 0.625rem;">
          <el-input v-model="msg" placeholder="请输入内容" size="mini" style="width: 37.5rem;" type="textarea" autosize></el-input>
          <el-button :disabled="!currentFriend.id" size="small" type="primary" class="sendBtn" @click="sendMsg">
            <i class="fa fa-send" style="margin-right: 0.9375rem;"></i>
            发送
          </el-button>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  data() {
    return {
      friends: [],//其他用户
      currentFriend: {}, //当前与当前用户聊天的用户
      msg: '',
      currentUser: this.$store.state.user
    };
  },
  computed:{
    isDotMap: {
      get: function() {
        return this.$store.state.isDotMap;
      }
    },
    msgList: {
      get: function() {
        return this.$store.state.msgList;
      }
    }
  },
  methods: {
    sendMsg() {
      var oldMsg = window.localStorage.getItem(this.$store.state.user.username + '#' + this.currentFriend.username);
      if (oldMsg == null) {
        oldMsg = [];
        oldMsg.push({ msg: this.msg, from: this.$store.state.user.username });
        window.localStorage.setItem(this.$store.state.user.username + '#' + this.currentFriend.username,JSON.stringify(oldMsg));
      } else {
        var oldMsgJson = JSON.parse(oldMsg);
        oldMsgJson.push({ msg: this.msg, from:  this.$store.state.user.username });
        window.localStorage.setItem(this.$store.state.user.username + '#' + this.currentFriend.username, JSON.stringify(oldMsgJson));
      }
      console.log("发送消息1：")
      this.$store.state.stomp.send('/ws/chat', {}, this.currentUser.username + ';' + this.msg + ';' + this.currentFriend.username);

      this.msg = '';
      this.loadChatDiv();
    },
    // 切换聊天页面
    toggleFriend(friend) {
      if (friend == this.currentFriend) {
        return;
      }
      this.currentFriend = friend;
      this.$store.commit('updateCurrentFriend', friend);
      this.loadChatDiv();
      // 删除标记
      this.$store.commit('removeValueDotMap', 'isDot#' + this.currentUser.username + '#' + friend.username);
      document.getElementById('chatDiv').scrollTop = document.getElementById('chatDiv').scrollHeight;
    },
    // 加载聊天页面。。并获取历史聊天记录
    loadChatDiv() {
      var oldMsg = window.localStorage.getItem(this.currentUser.username + '#' + this.currentFriend.username);
      if (oldMsg == null) {
        this.$store.commit('updateMsgList', []);
      } else {
        this.$store.commit('updateMsgList', JSON.parse(oldMsg));
      }
    },
    // 加载其他用户
    loadFriends() {
      var _this = this;
      this.getRequest('/chat/users').then(resp => {
        _this.friends = resp.data.obj;
      });
    }
  },
  watch: {
    msgList() {
      document.getElementById('chatDiv').scrollTop = document.getElementById('chatDiv').scrollHeight;
    }
  },
  mounted() {
    this.loadFriends();
  }
};
</script>

<style>
.chatDiv {
  border-color: #20a0ff;
  border-style: solid;
  border-radius: 5px;
  border-width: 1px;
  box-sizing: border-box;
  width: 700px;
  height: 450px;
  overflow-y: auto;
  padding-bottom: 50px;
}
.currentChatFriend {
  background-color: #dcdfe6;
}
.userfaseImg {
  width: 2.3125rem;
  height: 2.3125rem;
  border-radius: 1.875rem;
  margin-right: 0.625rem;
}
.friendListDiv {
  display: flex;
  justify-content: flex-start;
  padding-left: 1.25rem;
  box-sizing: border-box;
  align-items: center;
  width: 10rem;
  height: 2.5rem;
  border-color: #20a0ff;
  border-left-style: solid;
  border-top-style: solid;
  border-right-style: solid;
  border-width: 0.0625rem;
  cursor: pointer;
}
.sendBtn {
  padding-left: 25px;
  padding-right: 25px;
}

</style>
