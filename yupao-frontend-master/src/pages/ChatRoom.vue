<template>
  <div class="chat-container">
    <div>
      <scroll-view
          bind:scroll="onScroll"
          scroll-y
          id="scroller"
          style="height: 200px;"
      >
        <div
            v-for="message in messages"
            :key="message.id"
            :class="{'my-message': String(message.userId) === String(user.id), 'other-message': String(message.userId) !== String(user.id)}"
            class="message-container"
        >
          <img :src="message.userAvatar" alt="Avatar" class="avatar">
          <div class="message-content">
            <div class="message-header">
              <span class="username">{{ message.userName }}</span>
              <span class="timestamp">{{ formatDate(message.createTime) }}</span>
            </div>
            <p class="message-text">{{ message.text }}</p>
          </div>
        </div>
      </scroll-view>
    </div>
    <div class="input-container">
      <input v-model="text" type="text" placeholder="Type a message" class="message-input" />
      <button @click="sendMessage" class="send-button">Send</button>
    </div>
  </div>
</template>

<script setup >

import myAxios from "../plugins/myAxios";
import webSocketService from "../plugins/webSocket";
import {onMounted, ref} from "vue";
import {Toast} from "vant";
import {getCurrentUser} from "../services/user";

const user = ref();
const text = ref("hello world");
const messages = ref([]);


onMounted(async () => {
  user.value = await getCurrentUser();

  // 加载聊天记录
  const res =  await myAxios.get("/message/list");
  if(res.code!==0){
    Toast("请求成功");
  }else{
    Toast("请求失败");
  }

  messages.value = res.data;

  // 连接 websocket
  webSocketService.connect('ws://localhost:8080/api/websocket/{user.id}');
  // 添加回调函数
  webSocketService.onMessage((message) => {
    // message 是一个 json格式的字符串 , 做反序列化处理
    let data = JSON.parse(message)
    messages.value.push(data);
  });
  console.log(messages)
  this.sendJoinMessage();
})


const sendMessage = () => {
    // todo ： 发送给后端要不要封装类？
    const message = {
      // type: "message",
      userId: user.value.id,
      userName: user.value.username,
      userAvatar:user.value.avatarUrl,
      text: text.value,
    };
    const res =  myAxios.post("/message/add",message);
    webSocketService.sendMessage(message);
    // 发 http 请求 保存聊天记录
    text.value = "";
}
// 发送进入信息
const sendJoinMessage = () => {
  const message = {
    userId: this.userId,
    text: "joined the chat",
  };
  webSocketService.sendMessage(message);
}
// 发送离开信息
const sendLeaveMessage = () => {
  const message = {
    userId: this.userId,
    text: "left the chat",
  };
  webSocketService.sendMessage(message);
}

const formatDate = (date) => {
  return new Date(date).toLocaleTimeString([], { day:'2-digit' ,hour: '2-digit', minute: '2-digit' });
};

</script>


<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.message-container {
  display: flex;
  align-items: flex-start;
  margin-bottom: 15px;
  padding: 10px;
  border-radius: 8px;
}

.my-message {
  background-color: #e0f7fa;
  justify-content: flex-end;
}

.other-message {
  background-color: #f1f1f1;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.message-content {
  max-width: 70%;
}

.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.username {
  font-weight: bold;
  margin-right: 10px;
}

.timestamp {
  font-size: 0.8em;
  color: #999;
}

.message-text {
  margin: 0;
  word-wrap: break-word;
}

.input-container {
  display: flex;
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ddd;
  background-color: white;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.message-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-right: 10px;
}

.send-button {
  padding: 10px 20px;
  border: none;
  background-color: #007bff;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
}

.send-button:hover {
  background-color: #0056b3;
}
</style>
