<template>
  <el-container style="height: 87vh">
    <el-header style="height: 16vh;width: 185vh">
  <el-row style="height: 9vh" v-if="query === 'my'">
    <el-col :span="19" style="margin-right: 10px">
      <el-input v-model=search_title placeholder="请输入标题"></el-input>
    </el-col>
    <el-col :span="4" style="text-align: right">
      <el-button type="success" round @click="titlesearch">
        <el-icon style="vertical-align: middle;">
          <Search />
        </el-icon>
        <span style="vertical-align: middle;"> 搜索 </span>
      </el-button>
    </el-col>
  </el-row>
  <el-row style="height: 9vh" v-else>
    <el-col :span="19" style="margin-right: 10px">
      <el-input v-model=search_title placeholder="请输入标题"></el-input>
    </el-col>
    <el-col :span="4" style="text-align: right">
      <el-button type="success" round @click="titlesearchshare">
        <el-icon style="vertical-align: middle;">
          <Search />
        </el-icon>
        <span style="vertical-align: middle;"> 搜索 </span>
      </el-button>
    </el-col>
  </el-row>
      <el-row style="justify-content: left;text-align: left;height: 9vh">
    <el-col :span="16">
      <el-link type="info" :underline="false" style="text-align:left;vertical-align: middle;" href="#" @click="advancejump">
        高级搜索
      </el-link>
    </el-col>
    <el-col :span="8" style="text-align: right">
    <el-select
        v-model="value"
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
    </el-header>
    <el-main style="height: 100vh; width: 185vh">
      <el-scrollbar>
        <el-row
            v-for="(blog, index) in cblogList"
            :key="index"
            class="bordered-row"
            type="flex"
            justify="center"
            align="middle"
            style="margin-bottom: 15px;"
        >
          <el-col :span="24" class="centered-link">
            <el-link
                type="primary"
                :underline="false"
                style="font-size: 20px; vertical-align: middle; margin-bottom: 10px;"
                href="#"
                @click.prevent="handleClick(blog)"
            >
              {{ blog.title }}
            </el-link>
          </el-col>
          <el-col :span="12" style="text-align: left;">
            <el-text type="success">时间: {{ formatDate(blog.date) }}</el-text>
          </el-col>
          <el-col :span="12" style="text-align: right;">
            <el-text type="success">Stars: {{ blog.stars }}</el-text>
          </el-col>
        </el-row>
      </el-scrollbar>
    </el-main>
    <el-footer height="10vh" style="text-align:center;vertical-align: middle;">
      <el-row style="margin-top: 10px;justify-content: center;">
      <el-pagination background layout="prev, pager, next" :total=totalpage @current-change="pagechange"/>
      </el-row>
    </el-footer>
    </el-container>
</template>

<script>
import axios from 'axios';

export default {
  name: 'MyBlogPage',
  data() {
    return {
      search_title: '',
      rowCount: 0,  // 初始行数量为0
      blogList: [],
      cblogList: [],
      page: 0,
      value: '',
      totalpage: 10,
      query: 'my'
    };
  },
  created() {
    this.getuserid();
  },
  methods: {
    getuserid() {
      const sessionId = sessionStorage.getItem('session');
      console.log('session is ' + sessionId);
      if (sessionId && sessionId !== '-1') {
        axios.post('https://frp-fun.top:52702/user/test', null, {params: {sessionId}})
        .then(response => {
          this.userid = response.data;
          if (this.query === 'share'){
            this.searchShareBlog(response.data);
          }
          else if (this.query === 'my'){
            this.searchMyBlog(response.data);
          }
          else if (this.query === 'collect'){
            this.searchCollectBlog(response.data);
          }
          console.log('userid is ' + this.userid);
        })
        .catch(error => {
          console.error('Error checking login status:', error);
        });
      } else {
        console.error('Invalid session ID.');
      }
    },
    searchMyBlog() {
      const params = {
        userid: this.userid
      };
      console.log('now userid is ' + this.userid);
      axios.post('https://frp-fun.top:52702/article/myblogsearch', null, {params})  // 修改为你的实际后端API端点
      .then(response => {
        // 假设后端返回一个包含博客对象的列表
        this.blogList = response.data;
        console.log('blog is ' + (this.blogList[0] ? this.blogList[0].title : 'no blogs'));
        this.rowCount = this.blogList.length;
        console.log('row is ' + this.rowCount);
        let itemsPerPage = 10; // 每页显示的条目数量
        this.totalpage = Math.ceil(this.rowCount / itemsPerPage)*10;
        console.log('total is ' + this.totalpage);
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    searchShareBlog() {
      axios.post('https://frp-fun.top:52702/article/shareblog')  // 修改为你的实际后端API端点
      .then(response => {
        // 假设后端返回一个包含博客对象的列表
        this.blogList = response.data;
        console.log('blog is ' + (this.blogList[0] ? this.blogList[0].title : 'no blogs'));
        this.rowCount = this.blogList.length;
        console.log('row is ' + this.rowCount);
        let itemsPerPage = 10; // 每页显示的条目数量
        this.totalpage = Math.ceil(this.rowCount / itemsPerPage)*10;
        console.log('total is ' + this.totalpage);
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    searchCollectBlog() {
      const params = {
        userid: this.userid
      };
      axios.post('https://frp-fun.top:52702/article/collectblog',null,{params})  // 修改为你的实际后端API端点
      .then(response => {
        // 假设后端返回一个包含博客对象的列表
        this.blogList = response.data;
        console.log('blog is ' + (this.blogList[0] ? this.blogList[0].title : 'no blogs'));
        this.rowCount = this.blogList.length;
        console.log('row is ' + this.rowCount);
        let itemsPerPage = 10; // 每页显示的条目数量
        this.totalpage = Math.ceil(this.rowCount / itemsPerPage)*10;
        console.log('total is ' + this.totalpage);
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    handleClick(blog) {
      console.log('Clicked blog:', blog);
      console.log('q:', this.query);
      if (this.query === 'my') {
        // 这里添加你的逻辑，例如导航到博客详情页或执行其他操作
        this.$router.push({name: 'Show', params: {articleId: blog.id, query: 'my'}});
      }
      else{
        this.$router.push({name: 'Show', params: {articleId: blog.id, query: 'share'}});
      }
    },
    selectchange(){
      console.log(this.value);
      if (this.value === '按Stars从多到少') {
        this.blogList.sort((a, b) => {
          return b.stars - a.stars;
        });
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
      }
      else if (this.value === '按Stars从少到多') {
        this.blogList.sort((a, b) => {
          return a.stars - b.stars;
        });
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
      }
    },
    titlesearch(){
      if (this.search_title === ''){
        this.searchMyBlog();
        return;
      }
      const params = {
        userid: this.userid,
        title: this.search_title
      };
      console.log('now userid is ' + this.userid);
      axios.post('https://frp-fun.top:52702/article/myblogsearchbytitle', null, {params})  // 修改为你的实际后端API端点
      .then(response => {
        // 假设后端返回一个包含博客对象的列表
        this.blogList = response.data;
        console.log('blog is ' + (this.blogList[0] ? this.blogList[0].title : 'no blogs'));
        this.rowCount = this.blogList.length;
        console.log('row is ' + this.rowCount);
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
        this.search_title = ''
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    titlesearchshare(){
      if (this.search_title === ''){
        this.searchMyBlog();
        return;
      }
      const params = {
        title: this.search_title
      };
      console.log('now userid is ' + this.userid);
      axios.post('https://frp-fun.top:52702/article/shareblogtitle', null, {params})  // 修改为你的实际后端API端点
      .then(response => {
        // 假设后端返回一个包含博客对象的列表
        this.blogList = response.data;
        console.log('blog is ' + (this.blogList[0] ? this.blogList[0].title : 'no blogs'));
        this.rowCount = this.blogList.length;
        console.log('row is ' + this.rowCount);
        let startIndex = this.page * 10;
        let endIndex = startIndex + 10;
        this.cblogList = this.blogList.slice(startIndex, endIndex);
        this.search_title = ''
      })
      .catch(error => {
        console.error('Error fetching blog data:', error);
      });
    },
    advancejump(){
      this.$router.push('/advancedsearch');
    },
    pagechange(currentPage){
      this.page = currentPage-1;
      let startIndex = this.page * 10;
      console.log('now page is ' + this.page);
      let endIndex = startIndex + 10;
      this.cblogList = this.blogList.slice(startIndex, endIndex);
    },
    formatDate(dateTimeString) {
      const date = new Date(dateTimeString);
      return date.toISOString().slice(0, 19).replace('T', ' ');
    },
    updateQuery(query) {
      this.query = query;
      console.log('s os '+this.query);
      this.getuserid();
    }
  }
};
</script>

<style scoped>
.centered-link {
  text-align: center;
  vertical-align: middle;
}
.bordered-row {
  width: 100%; /* Adjust width to fit within its container */
  border: 1px solid #ebeef5;
  padding: 10px;
  border-radius: 4px;
  overflow: hidden; /* Prevent overflow */
}
/* Adjust el-row width and margins */
.el-row {
  width: 170vh; /* Ensure it fits within its container */
  margin-bottom: 10px; /* Adjust as needed */
}
</style>
