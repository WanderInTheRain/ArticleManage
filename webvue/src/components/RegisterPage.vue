<template>
  <div class="register-container" style="height:300px">
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
      <el-input type="password" v-model="confirmPassword" id="confirmPassword" style="width: 240px" placeholder="请确认密码" show-password>
        <template #prepend >
          <el-text style="width: 40px" type="primary">确认密码</el-text>
        </template>
      </el-input>
      <el-row style="text-align: center;">
        <el-button type="primary" round @click="handleRegister">注册</el-button>
      </el-row>
      <el-row style="text-align: center;">
        <el-link href="https://frp-fun.top:56561/login" type="warning">去登录</el-link>
      </el-row>
      <el-dialog
          v-model="iferrorm"
          width="500"
          :show-close="false"
      >
      <el-result
      icon="error"
      :title="errorMessage"
      >
      </el-result>
      </el-dialog>
      <el-dialog
          v-model="ifreg"
          width="500"
          :show-close="false"
      >
        <el-result
            icon="success"
            title="注册成功"
        >
        </el-result>
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
      confirmPassword: '',
      errorMessage: '',
      iferrorm: false,
      imageUrl: 'https://frp-fun.top:52702/article/image/logo.png',
      ifreg: false
    };
  },
  methods: {
    handleRegister() {
      // 检查密码是否一致
      if (this.password !== this.confirmPassword) {
        this.errorMessage = '密码和确认密码不一致';
        this.showerror();
        return;
      }
      const params = {
        username: this.username,
        password: this.password
      };
      axios.post('https://frp-fun.top:52702/user/registers', null, {params})
      .then(response => {
        this.errorMessage = response.data;
        if (this.errorMessage !== 'success'){
          console.log('m is '+ this.errorMessage)
          this.showerror();
        }
        else{
          this.showreg();
        }
      })
      .catch(error => {
        console.error('Error saving data:', error);
      });
    },
    showerror() {
      this.iferrorm = true;
      setTimeout(() => {
        this.iferrorm = false;
      }, 500); // 0.m5 seconds
    },
    showreg() {
      this.ifreg = true;
      setTimeout(() => {
        this.ifreg = false;
      }, 500); // 0.m5 seconds
    },
  }
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 20px;
  background-color: #f9f9f9;
  text-align: center; /* 将容器内的文本内容居中 */
}
</style>
