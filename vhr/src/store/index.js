import Vue from 'vue'
import Vuex from 'vuex'
import '../lib/sockjs'
import '../lib/stomp'
Vue.use(Vuex)

export default new Vuex.Store({
  // 1.state为vuex的基本数据
  state: {
    user: {
      name: window.localStorage.getItem('user' || '[]') == null ? '未登录' : JSON.parse(window.localStorage.getItem(
        'user' || '[]')).name,
      userfase: window.localStorage.getItem('user' || '[]') == null ? '未登录' : JSON.parse(window.localStorage.getItem(
        'user' || '[]')).userfase,
      username: window.localStorage.getItem('user' || '[]') == null ? '未登录' : JSON.parse(window.localStorage.getItem(
        'user' || '[]')).username,
      roles: window.localStorage.getItem('user' || '[]') == null ? '未登录' : JSON.parse(window.localStorage.getItem(
        'user' || '[]')).roles
    },
    stomp: null,
    nfDot: false,
    // 标记未读消息(exsit)
    isDotMap: new Map(),
    currentFriend: {},
    msgList: [],
  },
  mutations: {
    login(state, user) {
      // 登录时把登录信息先存在
      window.localStorage.setItem('user', JSON.stringify(user));
      state.user = JSON.parse(localStorage.getItem('user' || '[]'))
      console.log(state.user);
    },
    logout(state) {
      window.localStorage.removeItem('user');

    },
    addValue2DotMap(state, key) {
      state.isDotMap.set(key, "您有未读消息!");
    },
    toggleNFDot(state, newValue) {
      state.nfDot = newValue;
    },
    updateMsgList(state, newMsgList) {
      state.msgList = newMsgList;
    },
    // 更改当前正在聊天的好友
    updateCurrentFriend(state, newFriend) {
      state.currentFriend = newFriend;
    },
    removeValueDotMap(state, key) {
      state.isDotMap.delete(key);
    }
  },
  /**
   * Action 函数接受一个与 store 实例具有相同方法和属性的 context 对象，因此你可以调用 context.commit 提交一个 mutation，或者通过 context.state 和 context.getters 来获取 state 和 getters
   */
  actions: {
    connect(context) {
      context.state.stomp = Stomp.over(new SockJS("/ws/endpointChat"));
      context.state.stomp.connect({}, frame => {
        // 接受聊天信息(订阅)
        context.state.stomp.subscribe("/user/queue/chat", message => {
          var msg = JSON.parse(message.body);
          var oldMsg = window.localStorage.getItem(context.state.user.username + "#" + msg.from);
          if (oldMsg == null) {
            oldMsg = [];
            oldMsg.push(msg);
            window.localStorage.setItem(context.state.user.username + "#" + msg.from, JSON.stringify(oldMsg))
          } else {
            var oldMsgJson = JSON.parse(oldMsg);
            oldMsgJson.push(msg);
            window.localStorage.setItem(context.state.user.username + "#" + msg.from, JSON.stringify(
              oldMsgJson))
          }
          if (msg.from != context.state.currentFriend.username) {
            context.commit("addValue2DotMap", "isDot#" + context.state.username + "#" + msg.from);
          }
          // 更新msgList
          // 消息: 当前用户名#发送者
          var oldMsg2 = window.localStorage.getItem(context.state.user.username + "#" + context.state.currentFriend
            .username);
          if (oldMsg2 == null) {
            context.commit('updateMsgList', []);
          } else {
            context.commit('updateMsgList', JSON.parse(oldMsg2));
          }
        });
        // 系统通知
        context.state.stomp.subscribe("/topic/nf", message => {
          context.commit('toggleNFDot', true);
        });
      }, failedMsg => {

      });
    }
  }
});
