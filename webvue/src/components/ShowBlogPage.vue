<template>
  <el-container v-if="isLoggedIn">
    <el-header>
      <el-row v-if="this.query === 'my'">
        <el-col :span="16" style="display: flex; justify-content: left; align-items: center;margin-left: 50px;">
          <el-text style="font-size: 40px;">
            {{ this.blog.title }}
          </el-text>
        </el-col>
        <el-col :span="6" style="display: flex; justify-content: center; align-items: center;">
          <el-button type="primary" round @click="dialogFormVisible = true">
            <el-icon><EditPen /></el-icon>评论文章
          </el-button>
          <el-button type="primary" round @click="editpage">
            <el-icon><Edit /></el-icon>编辑文章
          </el-button>
          <el-button type="danger" round @click="goToHomepage">
            <el-icon><CloseBold /></el-icon>回到主页
          </el-button>
        </el-col>
        <el-dialog v-model="dialogFormVisible" title="评论文章" width="500px">
          <el-input
              v-model="textarea"
              style="width: 440px;margin-bottom: 20px;margin-left: 20px"
              :rows="7"
              type="textarea"
              placeholder="请输入评论"
          />
          <div style="text-align: right">
            <el-button type="primary" @click="this.savecomment">
              确定
            </el-button>
          </div>
        </el-dialog>
      </el-row>
      <el-row v-else>
        <el-col :span="16" style="display: flex; justify-content: left; align-items: center;margin-left: 50px;">
          <el-text style="font-size: 40px;">
            {{ this.blog.title }}
          </el-text>
        </el-col>
        <el-col :span="6" style="display: flex; justify-content: center; align-items: center;">
          <el-button type="primary" round @click="dialogFormVisible = true">
            <el-icon><EditPen /></el-icon>评论文章
          </el-button>
          <el-button v-if="this.ifcollect === false" type="primary" round @click="setcollect">
            <el-icon><Star /></el-icon>收藏文章
          </el-button>
          <el-button v-else type="success" round @click="deletecollect">
            <el-icon><Star /></el-icon>已收藏
          </el-button>
          <el-button type="danger" round @click="goToHomepage">
            <el-icon><CloseBold /></el-icon>回到主页
          </el-button>
        </el-col>
        <el-dialog v-model="dialogFormVisible" title="评论文章" width="500px">
          <el-input
              v-model="textarea"
              style="width: 440px;margin-bottom: 20px;margin-left: 20px"
              :rows="7"
              type="textarea"
              placeholder="请输入评论"
          />
          <div style="text-align: right">
            <el-button type="primary" @click="this.savecomment">
              确定
            </el-button>
          </div>
        </el-dialog>
      </el-row>
      <el-row style="margin-top: 15px">
        <el-col :span="8" style="text-align: center;">
          <el-text type="warning" size="large">分类: {{ this.blog.categoryname }}</el-text>
        </el-col>
        <el-col :span="8" style="text-align: center;">
          <el-text type="warning" size="large">时间: {{ this.formatDate(this.blog.date) }}</el-text>
        </el-col>
        <el-col :span="8" style="text-align: center;">
          <el-text type="warning" size="large">Stars: {{  this.blog.stars }}</el-text>
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <div ref="display" style="border: 1px solid #ccc;padding: 10px;">
        <p>{{ this.blog.content }}</p>
      </div>
      <el-divider></el-divider>
      <el-row>
        <el-col :span="24">
          <el-card>
            <div style="color: darkgrey;font-size: 25px">
              <span>评论区</span>
            </div>
            <div v-for="comment in comments" :key="comment" class="comment-section">
              <!-- Add your comment section content here -->
              <p>{{comment.content}}</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import Quill from 'quill';
import 'quill/dist/quill.snow.css';
import axios from 'axios';

export default {
  name: 'ShowBlogPage',
  data() {
    return {
      display: null,
      quill: null,
      articleId: '',
      blog: {
        title: '',
        content: '',
        categoryid: 0,
        categoryname: '',
        date: null,
        stars: 0
      },
      comments: null,
      dialogFormVisible: false,
      form: {},
      textarea: '',
      query: 'my',
      userid: '',
      ifcollect: false
    };
  },  
  computed: {
    isLoggedIn() {
      return sessionStorage.getItem('session') !== null;
    }
  },
  mounted() {
    this.comments = ['评论1','评论2']
    this.display = this.$refs.display;
    this.query = this.$route.params.query;
    if (this.display) {
      this.quill = new Quill(this.display, {
        theme: 'snow',
        modules: {
          toolbar: false
        },
        readOnly: true
      });
      this.getBlogContent();
      this.getComment();
      this.getuserid();
    }
  },
  methods: {
    getuserid() {
      const sessionId = sessionStorage.getItem('session');
      console.log('session is ' + sessionId);
      if (sessionId && sessionId !== '-1') {
        axios.post('https://frp-fun.top:52702/user/test', null, {params: {sessionId}})
        .then(response => {
          this.userid = response.data;
          console.log('userid is ' + this.userid);
          if (this.query === 'share'){
            this.notcollect();
          }
        })
        .catch(error => {
          console.error('Error checking login status:', error);
        });
      } else {
        console.error('Invalid session ID.');
      }
    },
    getBlogContent() {
      this.articleId = this.$route.params.articleId;
      const params = { articleid: this.articleId };
      axios.post('https://frp-fun.top:52702/article/getmyblog', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        this.blog = response.data;
        console.log('Blog loaded:', this.blog);
        this.quill.root.innerHTML = this.blog.content;
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    formatDate(dateTimeString) {
      const date = new Date(dateTimeString);
      return date.toISOString().slice(0, 19).replace('T', ' ');
    },
    goToHomepage() {
      // Implement navigation logic to homepage
      this.$router.push('/home')
    },
    getComment(){
      const params = { articleId: this.articleId };
      axios.post('https://frp-fun.top:52702/article/getcomment', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        this.getcatename();
        this.comments = response.data;
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    getcatename() {
      const params = { categoryId: this.blog.categoryid };
      axios.post('https://frp-fun.top:52702/article/getcategoryname', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        this.blog.categoryname = response.data;
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    savecomment(){
      const params = {
        articleId: this.articleId,
        content: this.textarea
      };
      axios.post('https://frp-fun.top:52702/article/savecomment', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        this.getComment();
        this.dialogFormVisible = false;
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    editpage(){
      this.$router.push({ name: 'EditBlog', params: { articleId: this.articleId, query: 'edit' } });
    },
    notcollect(){
      const params = {
        articleid: this.articleId,
        userid: this.userid
      };
      console.log('p is ' + this.userid);
      axios.post('https://frp-fun.top:52702/article/getcollectblogs', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        const res = response.data;
        if (res === 'success'){
          this.ifcollect = true;
        }
        else {
          this.ifcollect =  false;
        }
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    setcollect(){
      const params = {
        articleid: this.articleId,
        userid: this.userid
      };
      axios.post('https://frp-fun.top:52702/article/setcollectblog', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        const res = response.data;
        if (res === 'success'){
          this.ifcollect = true;
          this.blog.stars += 1;
        }
        else {
          this.ifcollect = false;
        }
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    deletecollect(){
      const params = {
        articleid: this.articleId,
        userid: this.userid
      };
      axios.post('https://frp-fun.top:52702/article/deletecollectblog', null, { params })
      .then(response => {
        console.log('res loaded:', response.data);
        const res = response.data;
        if (res === 'success'){
          this.ifcollect = false;
          this.blog.stars -= 1;
        }
        else {
          this.ifcollect = true;
        }
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
  }
};
</script>

<style scoped>
.comment-section {
  color: cornflowerblue;
  border: 1px solid #ccc;
  padding: 10px;
  margin-top: 10px;
}
</style>
