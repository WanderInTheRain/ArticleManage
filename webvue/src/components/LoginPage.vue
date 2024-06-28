<template>
  <div class="login-container" style="height: 250px">
      <el-space direction="vertical" :size="10">
        <el-image style="width: 100px; height: 100px" :src="imageUrl"/>
        <el-input v-model="username" id="username" style="width: 240px" placeholder="请输入用户名" clearable>
          <template #prepend >
            <el-text style="width: 40px" type="primary">用户名</el-text>
          </template>
        </el-input>
        <el-input type="password" v-model="password" id="password" style="width: 240px" placeholder="请输入密码" show-password>
          <template #prepend >
            <el-text style="width: 40px" type="primary">密码</el-text>
          </template>
        </el-input>
        <el-row style="text-align: center;">
        <el-button type="primary" round @click="handleLogin">登录</el-button>
        </el-row>
      <el-row style="text-align: center;">
        <el-link href="https://frp-fun.top:56561/register" type="warning">去注册</el-link>
      </el-row>
      <el-dialog
          v-model="errorMessage"
          width="500"
          :show-close="false"
      >
        <el-result
            icon="error"
            title="用户名或密码错误"
        ></el-result>
      </el-dialog>
      </el-space>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      errorMessage: false,
      imageUrl: 'https://frp-fun.top:52702/article/image/logo.png',  // 正确的图片 URL
      session: ''
    };
  },
  methods: {
    handleLogin() {
      const params = {
        username: this.username,
        password: this.password
        
      };
      axios.post('https://frp-fun.top:52702/user/logins', null, {params})
      .then(response => {
        this.session = response.data;
        console.log('s is  ' + this.session)
        if (this.session != '-1') {
          // 登录成功，处理逻辑
          //const ss = this.session
          sessionStorage.setItem('session', this.session);
          this.$router.push('/home');
        }
        else {
          this.showerror();
        }
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    showerror() {
      this.errorMessage = true;
      setTimeout(() => {
        this.errorMessage = false;
      }, 500); // 0.5 seconds
    }
  }
};
</script>

<style>

.login-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 20px;
  background-color: #f9f9f9;
  text-align: center; /* 将容器内的文本内容居中 */
}
</style>
