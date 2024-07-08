<template>
  <form @submit.prevent="handelSubmit">

    <label>Account:</label>
    <input type="text" v-model="userAccount" required>

    <label>Password:</label>
    <input type="password" v-model="userPassword" required>

    <label>ConfirmedPassword:</label>
    <input type="password" v-model="confirmedPassword" required>

    <label>Role:</label>
    <select v-model="role">
      <option value="developer">Student</option>
      <option value="designer">Socialist</option>
    </select>

    <label>tag:(press "," to add)</label>
    <input type="text" v-model="tag" @keyup="addtags">
    <div class="pill" v-for="skill in tags" :key="skill">
      <span @click="deleteTag(skill)">{{skill}}</span>
    </div>

    <div class="terms">
      <input type="checkbox" v-model="terms" required>
      <label>Accept terms and conditions</label>
    </div>

    <div class="submit">
      <button @click="register">Creat an Account</button>
    </div>
    
    <h1> {{tagsJson}}</h1>
  </form>

</template>

<script>
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import {useRouter} from "vue-router" ;
export default {
  data(){
    return{
      userAccount:'',
      userPassword:'',
      confirmedPassword:'',
      role:'Student',
      tags:[],
      tag:'',
    }
  },
  computed: {
    tagsJson() {
      return JSON.stringify(this.tags);
    }
  },
  methods:
      {
        addtags($event){
          if($event.key === ',' && this.tag)
          {
            if(!this.tags.includes(this.tag))
            {
              this.tags.push(this.tag)
            }
            this.tag = ''
          }

        },
        deleteTag(skill)
        {
          this.tags = this.tags.filter(item => {
            return skill !==item
          })
        },

        async fetchHelloWorld()
        {
          try {
            const response = await myAxios.get('http://localhost:8083/users/hello')
            this.message = response.data;
          } catch (error) {
            console.error('Failed to fetch hello world data:', error);
          }
        },
        // 发送注册请求
        async register(){
          const userRegisterRequest = {
            userAccount:this.userAccount,
            userPassword:this.userPassword,
            checkPassword:this.confirmedPassword,
            tags:this.tagsJson,
          }
          const response = await myAxios.post('/user/register',userRegisterRequest)
          if (response?.code === 0 && response.data){
            Toast.success('注册成功');
            await useRouter().push({
              path: '/user/login',
              replace: true,
            });
          } else {
            Toast.success('添加失败');
          }
        }

      }

}
</script>

<style scoped>
form {
  max-width: 420px;
  margin: 30px auto;
  background: white;
  text-align: left;
  padding: 40px;
  border-radius: 10px;
}
label {
  color: #aaa;
  display: inline-block;
  margin: 25px 0 15px;
  font-size: 0.6em;
  text-transform: uppercase;
  letter-spacing: 1px;
  font-weight: bold;
}
input, select {
  display: block;
  padding: 10px 6px;
  width: 100%;
  box-sizing: border-box;
  border: none;
  border-bottom: 1px solid #ddd;
  color: #555;
}
input[type="checkbox"] {
  display: inline-block;
  width: 16px;
  margin: 0 10px 0 0;
  position: relative;
  top: 2px;
}
.pill {
  display: inline-block;
  margin: 20px 10px 0 0;
  padding: 6px 12px;
  background: #eee;
  border-radius: 20px;
  font-size: 12px;
  letter-spacing: 1px;
  font-weight: bold;
  color: #777;
  cursor: pointer;
}
button {
  background: #0b6dff;
  border: 0;
  padding: 10px 20px;
  margin-top: 20px;
  color: white;
  border-radius: 20px;
}
.submit {
  text-align: center;
}
.error {
  color: #ff0062;
  margin-top: 10px;
  font-size: 0.8em;
  font-weight: bold;
}
</style>