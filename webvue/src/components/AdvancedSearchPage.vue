<template>
  <el-container v-if="isLoggedIn">
    <el-header height="40vh">
      <el-text type="primary" style="font-size: 30px;"> 高级搜索 </el-text>
      <el-form :inline="true" style="margin-top: 20px;">
        <el-row>
          <el-col :span="14">
            <el-form-item label="作者" style="width: 500px;">
              <el-input v-model="author" placeholder="请输入作者"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="标题" style="width: 340px;">
              <el-input v-model="title" placeholder="请输入标题"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="14">
            <el-form-item label="内容" style="width: 500px;">
              <el-input v-model="content" placeholder="请输入内容"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="范围">
              <el-select v-model="searchType" placeholder="请选择查找方式" style="width: 300px">
                <el-option label="我的文章" value="myArticles"></el-option>
                <el-option label="共享文章" value="sharedArticles"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="2" style="text-align: right; width: 105px;">
            <el-button type="danger" round @click="backhome">
              <el-icon><CloseBold /></el-icon>回到主页
            </el-button>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item label="时间" style="width: 500px;">
              <el-date-picker
                  v-model="date"
                  type="daterange"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="YYYY-MM-DD"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分类">
              <el-tree-select
                  v-model="value"
                  :data="data"
                  check-strictly
                  :render-after-expand="false"
                  style="width: 300px"
              />
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button type="success" round @click="handldsearch" style="width: 105px;">
              <el-icon style="vertical-align: middle;">
                <Search />
              </el-icon>
              <span style="vertical-align: middle;"> 搜索 </span>
            </el-button>
          </el-col>
          <el-col :span="24" style="text-align: right">
            <el-select
                v-model="select"
                placeholder="排序方式"
                size="default"
                style="width: 240px"
                @change="selectchange"
            >
              <el-option value="按Stars从多到少">按Stars从多到少</el-option>
              <el-option value="按Stars从少到多">按Stars从少到多</el-option>
            </el-select>
          </el-col>
        </el-row>
      </el-form>
    </el-header>
    <el-main>
      <el-row v-for="(blog, index) in blogList" :key="index" class="bordered-row" type="flex" justify="space-between" align="middle">
        <el-col :span="24" style="text-align: center">
          <el-link type="primary" :underline="false" style="text-align: center;font-size:20px;" href="#" @click.prevent="handleClick(blog)">{{ blog.title }}</el-link>
        </el-col>
        <el-col :span="12">
          <el-text type="success">时间: {{ blog.date }}</el-text>
        </el-col>
        <el-col :span="12" style="text-align: right">
          <el-text type="success">Stars: {{ blog.stars }}</el-text>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      author: '',
      title: '',
      date: [],
      startdate: null,
      enddate: null,
      blogList: [],
      data: [],
      value: '所有',
      select: '',
      searchType: 'sharedArticles',
      userid: '',
      content: ''
    };
  },
  computed: {
    isLoggedIn() {
      return sessionStorage.getItem('session') !== null;
    }
  },
  mounted() {
    this.getuserid();
    this.getcategorytree();
  },
  methods: {
    handldsearch() {
      const params = {
        userid: this.userid,
        author: this.author,
        title: this.title,
        content: this.content,
        startdate: this.date[0],
        enddate: this.date[1],
        categoryid: this.value,
        searchType: this.searchType // Include the search type in the request
      };
      console.log(params)
      axios.post('https://frp-fun.top:52702/article/advancedsearchshare', null, { params })
      .then((response) => {
        this.blogList = response.data;
      })
      .catch((error) => {
        console.error('Error getting category tree:', error);
      });
    },
    handleClick(blog) {
      console.log('Blog clicked:', blog);
      if (this.searchType === 'myArticles') {
        // 这里添加你的逻辑，例如导航到博客详情页或执行其他操作
        this.$router.push({name: 'Show', params: {articleId: blog.id, query: 'my'}});
      }
      else{
        this.$router.push({name: 'Show', params: {articleId: blog.id, query: 'share'}});
      }
    },
    getcategorytree() {
      axios.post('https://frp-fun.top:52702/article/getcategorytree')
      .then((response) => {
        const proxy = response.data;
        this.data = proxy.children.map((child) => ({
          value: child.value,
          label: child.label,
          children: child.children,
        }));
        this.data.push({ value: '0', label: '所有', children: [] });
        this.value = '0'
        console.log(this.data);
      })
      .catch((error) => {
        console.error('Error getting category tree:', error);
      });
    },
    backhome() {
      this.$router.push('/home');
    },
    getuserid() {
      const sessionId = sessionStorage.getItem('session');
      console.log('session is ' + sessionId);
      if (sessionId && sessionId !== '-1') {
        axios.post('https://frp-fun.top:52702/user/test', null, {params: {sessionId}})
        .then(response => {
          this.userid = response.data;
          this.searchMyBlog(response.data);
          console.log('userid is ' + this.userid);
        })
        .catch(error => {
          console.error('Error checking login status:', error);
        });
      } else {
        console.error('Invalid session ID.');
      }
    },
  },
};
</script>

<style scoped>
.bordered-row {
  border: 1px solid #ebeef5;
  padding: 10px;
  border-radius: 4px;
  overflow: hidden;
  margin-top: 10px;
}
</style>
