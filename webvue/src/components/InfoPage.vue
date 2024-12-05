<template>
    <el-container style="height: 87vh; padding: 10px;">
      <!-- 顶部个人信息部分 -->
      <el-header style="height: 15vh;">
        <h2>个人信息</h2>
        <div>
          <p><strong>用户名:</strong> {{ this.username }}</p>
          <!-- 其他个人信息字段 -->
        </div>
      </el-header>
  
      <!-- 主体部分，包含修改密码和权限管理 -->
      <el-main style="height: 70vh;">
        <el-card>
          <el-form :model="formData" label-width="100px" @submit.prevent="handleSubmit">
            <el-form-item label="当前密码" prop="current">
              <el-input type="password" v-model="formData.current" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="new">
              <el-input type="password" v-model="formData.new" show-password></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmNew">
              <el-input type="password" v-model="formData.confirmNew" show-password></el-input>
            </el-form-item>
            <el-form-item label="读写权限">
              <el-radio-group v-model="formData.readWrite">
                <el-radio label="readonly">只读</el-radio>
                <el-radio label="write">可写</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" native-type="submit">提交修改</el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
      <el-dialog
      v-model="dialogVisible"
      width="500"
      :show-close="false"
  >
    <el-result
        icon="success"
        title="保存成功"
    ></el-result>
  </el-dialog>
  <el-dialog
      v-model="deleteVisible"
      width="500"
      :show-close="false"
  >
    <el-result
        icon="error"
        :title=errmsg
    ></el-result>
  </el-dialog>
      </el-main>
    </el-container>
  </template>

  <script>
import axios from 'axios';

export default {
  name: 'InfoPage',
  data() {
    return {
      userInfo: {
        username: '用户名'
      },
      formData: {
        current: '',
        new: '',
        confirmNew: '',
        readWrite: 'write' // 默认设置为只读
      },
      dialogVisible: false, // 控制显示修改密码部分
      deleteVisible: false,
      userid: '',
      username: '',
      errmsg: '' // 控制显示权限管理部分
    };
  },
  mounted() {
    this.getuserid();
  },
  methods: {
    handleSubmit() {
        // 检查新密码和确认密码是否匹配
        if (this.formData.new !== this.formData.confirmNew) {
          
          this.errmsg = '两次密码不匹配'
          this.showerror();
          return;
        }

        // 提交修改密码的逻辑，可以调用后端API
        // 调用后端API更新密码
        const params = {
          userid: this.userid,
          username: this.username,
          password: this.formData.new,
          oldpassword: this.formData.current,
          permissions: this.formData.readWrite
        };

        axios.post('https://frp-fun.top:52702/user/saveuser', null, {params})
        .then(response => {
          const res = response.data;
          console.log('rs '+res);
          if (res === 'success'){
            this.showsuccess();
          }
          else{
            this.errmsg = '密码错误'
            this.showerror();
          }
        })
        .catch(error => {
          console.error('Error saving data:', error);
        });
    },
    getuserid(){
      const sessionId = sessionStorage.getItem('session');
      console.log('session is ' + sessionId);
      if (sessionId && sessionId !== '-1') {
        axios.post('https://frp-fun.top:52702/user/test', null, {params: {sessionId}})
        .then(response => {
          this.userid = response.data;
          console.log('userid is ' + this.userid);
          this.getusername();
        })
        .catch(error => {
          console.error('Error checking login status:', error);
        });
      } else {
        console.error('Invalid session ID.');
      }
    },
    getusername() {
        axios.post('https://frp-fun.top:52702/user/getusername', null, {params: {userid: this.userid}})
        .then(response => {
          this.username = response.data;
          console.log('userid is ' + this.userid);
        })
        .catch(error => {
          console.error('Error checking login status:', error);
        });
      },
    showsuccess() {
      this.dialogVisible = true;
      setTimeout(() => {
        this.dialogVisible = false;
      }, 500); // 0.5 seconds
    },
    showerror(){
      this.deleteVisible = true;
      setTimeout(() => {
        this.deleteVisible = false;
      }, 500); // 0.5 seconds
    }
  }
};
</script>
