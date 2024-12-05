<template>
  <el-container style="height: 95vh;" v-if="isLoggedIn">
    <el-aside width="30vh" style="height: 97vh;">
      <div style="text-align: center;">
        <el-image style="width: 120px; height: 120px;" :src="imageUrl"></el-image>
      </div>
      <el-menu :default-active="activeMenu" @select="handleSelect">
        <el-menu-item :class="{ 'active-menu-item': activeMenu === 'createblog' }" index="createblog">
          <el-text type="warning" class="side_text">创建博客</el-text>
        </el-menu-item>
        <el-menu-item :class="{ 'active-menu-item': activeMenu === 'viewblog' }" index="viewblog">
          <el-text type="warning" class="side_text">浏览博客</el-text>
        </el-menu-item>
        <el-menu-item :class="{ 'active-menu-item': activeMenu === 'myblog' }" index="myblog">
          <el-text type="warning" class="side_text">我的博客</el-text>
        </el-menu-item>
        <el-menu-item :class="{ 'active-menu-item': activeMenu === 'collectblog' }" index="collectblog">
          <el-text type="warning" class="side_text">我的收藏</el-text>
        </el-menu-item>
        <el-menu-item :class="{ 'active-menu-item': activeMenu === 'info' }" index="info">
          <el-text type="warning" class="side_text">我的信息</el-text>
        </el-menu-item>
        <el-menu-item :class="{ 'active-menu-item': activeMenu === 'quit' }" index="quit">
          <el-text type="warning" class="side_text">退出登录</el-text>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-main style="height: 100%">
      <component :is="currentComponent" ref="myBlogComponent" />
    </el-main>
  </el-container>
  <div v-else>
    <h2 style="text-align: left; color: red">请先登录</h2>
  </div>
</template>

<script>
import LoginPage from "@/components/LoginPage.vue";
import MyBlogPage from "@/components/MyBlogPage.vue";
import InfoPage from "@/components/InfoPage.vue";
import axios from "axios";

export default {
  name: "MenuPage",
  components: {
    LoginPage,
    MyBlogPage,
    InfoPage
  },
  data() {
    return {
      userid: "",
      activeMenu: "viewblog", // Default active menu item
      currentComponent: "MyBlogPage", // Default component
      imageUrl: "https://frp-fun.top:52702/article/image/logo.png", // Update with a valid image URL
      isLoggedIn: false, // Initialize login status
      myBlogComponent: null,
    };
  },
  created() {
    this.checkLoginStatus();
  },
  methods: {
    checkLoginStatus() {
      const sessionId = sessionStorage.getItem("session");
      console.log("session is" + sessionId);
      if (sessionId !== "-1" && sessionId !== null) {
        axios
          .post("https://frp-fun.top:52702/user/test", null, { params: { sessionId } })
          .then((response) => {
            this.userid = response.data;
            console.log("userid is" + this.userid);
            this.isLoggedIn = this.userid !== "-1";
            if (this.isLoggedIn) {
              this.handleSelect(this.activeMenu); // Ensure the default component is loaded
            }
          })
          .catch((error) => {
            console.error("Error checking login status:", error);
            this.isLoggedIn = false;
          });
      } else {
        this.isLoggedIn = false;
      }
    },
    handleSelect(key) {
      this.activeMenu = key;
      switch (key) {
        case "info": {
          this.currentComponent = "InfoPage";
          break;
        }
        case "collectblog": {
          this.currentComponent = "MyBlogPage";
          this.$nextTick(() => {
            this.$refs.myBlogComponent.updateQuery("collect");
          });
          break;
        }
        case "viewblog": {
          this.currentComponent = "MyBlogPage";
          this.$nextTick(() => {
            this.$refs.myBlogComponent.updateQuery("share");
          });
          break;
        }
        case "myblog": {
          this.currentComponent = "MyBlogPage";
          this.$nextTick(() => {
            this.$refs.myBlogComponent.updateQuery("my");
          });
          break;
        }
        case "createblog": {
          this.$router.push({ name: "EditBlog", params: { articleId: this.articleId, query: "create" } });
          break;
        }
        case "quit": {
          const params = {
            sessionId: sessionStorage.getItem("session"),
          };
          // Using async/await syntax for asynchronous operations
          axios
            .post("https://frp-fun.top:52702/user/logout", null, { params })
            .then((response) => {
              console.log(response.data);
              sessionStorage.removeItem("session")
            })
            .catch((error) => {
              console.error("Error logging out:", error);
            });
          this.$router.push("login");
          break;
        }
        default: {
          this.currentComponent = "MyBlogPage";
        }
      }
    },
  },
};
</script>

<style scoped>
.side_text {
  text-align: center;
  font-size: 20px;
  width: 40vh;
  font-family: "Microsoft YaHei", Arial, sans-serif;
}
.active-menu-item {
  background-color: #c4e9eb; /* 这里设置你想要的背景颜色 */
}
</style>
